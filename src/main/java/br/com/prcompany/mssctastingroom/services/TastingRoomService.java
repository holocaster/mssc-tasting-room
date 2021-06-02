package br.com.prcompany.mssctastingroom.services;

import br.com.prcompany.beerevents.exceptions.ObjectNotFoundException;
import br.com.prcompany.beerevents.model.BeerOrderDTO;
import br.com.prcompany.beerevents.model.CustomerDto;
import br.com.prcompany.mssctastingroom.domain.TastingRoom;
import br.com.prcompany.mssctastingroom.exceptions.OrderNotInsertedException;
import br.com.prcompany.mssctastingroom.repository.TastingRoomRepository;
import br.com.prcompany.mssctastingroom.services.customer.CustomerFeignClient;
import br.com.prcompany.mssctastingroom.web.mappers.TastingRoomMapper;
import br.com.prcompany.mssctastingroom.web.model.TastingRoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TastingRoomService {

    private final TastingRoomRepository tastingRoomRepository;

    private final CustomerFeignClient customerFeignClient;

    private final TastingRoomMapper mapper;

    public TastingRoomDTO createTastingRoom(UUID customerId) {

        final ResponseEntity<CustomerDto> customer = this.customerFeignClient.getCustomer(customerId);

        if (customer.getBody() == null) {
            throw new ObjectNotFoundException("Customer not found with id: " + customerId);
        }

        final BeerOrderDTO beerOrderDTO = this.customerFeignClient.saveOrder(customerId, BeerOrderDTO.builder().build());

        if (beerOrderDTO == null) {
            throw new OrderNotInsertedException("Order can not be inserted for customer: " + customerId);
        }

        TastingRoom tastingRoom = TastingRoom.builder().customerId(customerId).build();
        tastingRoom.setId(null);

        tastingRoom = this.tastingRoomRepository.saveAndFlush(tastingRoom);

        return this.mapper.tastingRoomToDTO(tastingRoom);
    }
}
