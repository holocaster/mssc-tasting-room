package br.com.prcompany.mssctastingroom.services;

import br.com.prcompany.beerevents.exceptions.ObjectNotFoundException;
import br.com.prcompany.beerevents.model.BeerDTO;
import br.com.prcompany.beerevents.model.BeerOrderDTO;
import br.com.prcompany.beerevents.model.BeerOrderLineDTO;
import br.com.prcompany.beerevents.model.CustomerDto;
import br.com.prcompany.mssctastingroom.domain.TastingRoom;
import br.com.prcompany.mssctastingroom.exceptions.OrderNotInsertedException;
import br.com.prcompany.mssctastingroom.repository.TastingRoomRepository;
import br.com.prcompany.mssctastingroom.services.beer.BeerService;
import br.com.prcompany.mssctastingroom.services.customer.CustomerFeignClient;
import br.com.prcompany.mssctastingroom.web.mappers.TastingRoomMapper;
import br.com.prcompany.mssctastingroom.web.model.TastingRoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.naming.ServiceUnavailableException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TastingRoomService {

    private final TastingRoomRepository tastingRoomRepository;

    private final CustomerFeignClient customerFeignClient;

    private final TastingRoomMapper mapper;

    private final BeerService beerService;

    public TastingRoomDTO createTastingRoom(UUID customerId, String upc) throws ServiceUnavailableException {

        final ResponseEntity<CustomerDto> customer = this.customerFeignClient.getCustomer(customerId);

        if (HttpStatus.SERVICE_UNAVAILABLE.equals(customer.getStatusCode())) {
            throw new ServiceUnavailableException("Customer service is not available");
        }

        if (customer.getBody() == null) {
            throw new ObjectNotFoundException("Customer not found with id: " + customerId);
        }

        Optional<BeerDTO> beerDTOOptional;
        try {
            beerDTOOptional = this.beerService.getBeerByUpc(upc);
            if (!beerDTOOptional.isPresent()) {
                throw new ObjectNotFoundException(MessageFormat.format("Beer with upc {0} not found ", upc));
            }
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new ObjectNotFoundException(MessageFormat.format("Beer with upc {0} not found ", upc));
        }

        BeerOrderDTO beerOrderDTO = BeerOrderDTO.builder().build();
        beerOrderDTO.setCustomerId(customerId);
        beerOrderDTO.setBeerOrderLines(Arrays.asList(BeerOrderLineDTO.builder().upc(upc).beerId(beerDTOOptional.get().getId()).build()));

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
