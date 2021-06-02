package br.com.prcompany.mssctastingroom.domain;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TastingRoom extends BaseEntity {

    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID customerId;


}
