package br.com.prcompany.mssctastingroom.services.customer;

import br.com.prcompany.beerevents.model.BeerOrderDTO;
import br.com.prcompany.beerevents.model.CustomerDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(name = "${beer_order_service_name}")
public interface CustomerFeignClient {

    String CIRCUIT_BREAKER = "CustomerFeignClientBreaker";

    @RequestMapping(method = RequestMethod.GET, value = "${customer_path}")
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallback")
    ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID customerId);

    @RequestMapping(method = RequestMethod.POST, value = "${order_path}")
    @CircuitBreaker(name = CIRCUIT_BREAKER, fallbackMethod = "fallbackSaveOrder")
    BeerOrderDTO saveOrder(@PathVariable UUID customerId, @RequestBody BeerOrderDTO beerOrderDTO);


    default ResponseEntity<CustomerDto> fallback(Throwable t) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }

    default BeerOrderDTO fallbackSaveOrder(Throwable t) {
        return null;
    }
}
