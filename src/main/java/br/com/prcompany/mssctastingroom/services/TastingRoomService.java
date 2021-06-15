package br.com.prcompany.mssctastingroom.services;

import br.com.prcompany.beerevents.exceptions.ObjectNotFoundException;
import br.com.prcompany.beerevents.model.BeerOrderDTO;
import br.com.prcompany.beerevents.model.BeerOrderLineDTO;
import br.com.prcompany.beerevents.model.CustomerDto;
import br.com.prcompany.mssctastingroom.domain.TastingRoom;
import br.com.prcompany.mssctastingroom.exceptions.OrderNotInsertedException;
import br.com.prcompany.mssctastingroom.repository.TastingRoomRepository;
import br.com.prcompany.mssctastingroom.services.customer.CustomerFeignClient;
import br.com.prcompany.mssctastingroom.web.mappers.TastingRoomMapper;
import br.com.prcompany.mssctastingroom.web.model.TastingRoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TastingRoomService {

    private final TastingRoomRepository tastingRoomRepository;

    private final CustomerFeignClient customerFeignClient;

    private final TastingRoomMapper mapper;

    public TastingRoomDTO createTastingRoom(UUID customerId) throws ServiceUnavailableException {

        final ResponseEntity<CustomerDto> customer = this.customerFeignClient.getCustomer(customerId);

        if (HttpStatus.SERVICE_UNAVAILABLE.equals(customer.getStatusCode())) {
            throw new ServiceUnavailableException("Customer service is not available");
        }

        if (customer.getBody() == null) {
            throw new ObjectNotFoundException("Customer not found with id: " + customerId);
        }

        BeerOrderDTO beerOrderDTO = BeerOrderDTO.builder().build();
        beerOrderDTO.setBeerOrderLines(Arrays.asList(BeerOrderLineDTO.builder().upc("0631234200036").build()));

        beerOrderDTO = this.customerFeignClient.saveOrder(customerId, beerOrderDTO);

        if (beerOrderDTO == null) {
            throw new OrderNotInsertedException("Order can not be inserted for customer: " + customerId);
        }

        TastingRoom tastingRoom = TastingRoom.builder().customerId(customerId).build();
        tastingRoom.setId(null);

        tastingRoom = this.tastingRoomRepository.saveAndFlush(tastingRoom);

        return this.mapper.tastingRoomToDTO(tastingRoom);
    }
}
