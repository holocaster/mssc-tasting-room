package br.com.prcompany.mssctastingroom.services;

import br.com.prcompany.beerevents.exceptions.ObjectNotFoundException;
import br.com.prcompany.beerevents.model.BeerOrderDTO;
import br.com.prcompany.beerevents.model.CustomerDto;
import br.com.prcompany.mssctastingroom.MsscTastingRoomApplication;
import br.com.prcompany.mssctastingroom.exceptions.OrderNotInsertedException;
import br.com.prcompany.mssctastingroom.services.customer.CustomerFeignClient;
import br.com.prcompany.mssctastingroom.web.model.TastingRoomDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.EurekaClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.ServiceUnavailableException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MsscTastingRoomApplication.class)
class TastingRoomServiceTest {

    private static final String TESTE = "TESTE";

    @Autowired
    private TastingRoomService tastingRoomService;

    @MockBean
    private CustomerFeignClient customerFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Lazy
    @Autowired
    private EurekaClient eurekaClient;

    @Value("${customer_path}")
    private String customerPath;

    @Test
    void createTastingRoomServiceUnavailable() {
        Mockito.when(this.customerFeignClient.getCustomer(Mockito.any(UUID.class))).thenReturn(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
        assertThrows(ServiceUnavailableException.class, () -> {
            this.tastingRoomService.createTastingRoom(UUID.randomUUID(), TESTE);
        });
    }

    @Test
    void createTastingRoomCustomerNotFound() throws JsonProcessingException {
        Mockito.when(this.customerFeignClient.getCustomer(Mockito.any(UUID.class))).thenReturn(ResponseEntity.notFound().build());
        assertThrows(ObjectNotFoundException.class, () -> {
            this.tastingRoomService.createTastingRoom(UUID.randomUUID(), TESTE);
        });
    }

    @Test
    void createTastingRoomOrderNotInserted() {
        Mockito.when(this.customerFeignClient.getCustomer(Mockito.any(UUID.class))).thenReturn(ResponseEntity.ok(CustomerDto.builder().build()));
        Mockito.when(this.customerFeignClient.saveOrder(Mockito.any(UUID.class) ,Mockito.any(BeerOrderDTO.class))).thenReturn(null);
        assertThrows(OrderNotInsertedException.class, () -> {
            this.tastingRoomService.createTastingRoom(UUID.randomUUID(), TESTE);
        });
    }

    @Test
    void createTastingRoomCustomer() throws ServiceUnavailableException {
        Mockito.when(this.customerFeignClient.getCustomer(Mockito.any(UUID.class))).thenReturn(ResponseEntity.ok(CustomerDto.builder().build()));
        Mockito.when(this.customerFeignClient.saveOrder(Mockito.any(UUID.class) ,Mockito.any(BeerOrderDTO.class))).thenReturn(BeerOrderDTO.builder().build());

        TastingRoomDTO tastingRoomDTO = this.tastingRoomService.createTastingRoom(UUID.randomUUID(), TESTE);

        assertNotNull(tastingRoomDTO.getCustomerId());
    }

}