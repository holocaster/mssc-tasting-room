package br.com.prcompany.mssctastingroom.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class TastingRoomDTO implements Serializable {

    @Null
    private UUID id;

    @Null
    private Integer version;

    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;

    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate;

    private UUID customerId;


}
