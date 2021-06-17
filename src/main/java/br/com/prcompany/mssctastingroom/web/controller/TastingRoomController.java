package br.com.prcompany.mssctastingroom.web.controller;

import br.com.prcompany.mssctastingroom.services.TastingRoomService;
import br.com.prcompany.mssctastingroom.web.model.TastingRoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.naming.ServiceUnavailableException;
import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tastingroom")
@Slf4j
public class TastingRoomController {

    @Autowired
    private TastingRoomService tastingRoomService;

    @PostMapping("{customerId}/{upc}")
    public ResponseEntity<Void> createTastingRoom(@PathVariable("customerId") UUID customerId, @PathVariable("upc") String upc) throws ServiceUnavailableException {
        log.debug("Calling REST createTastingRoom");
        final TastingRoomDTO tastingRoomDTO = this.tastingRoomService.createTastingRoom(customerId, upc);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tastingRoomDTO.getId().toString()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
