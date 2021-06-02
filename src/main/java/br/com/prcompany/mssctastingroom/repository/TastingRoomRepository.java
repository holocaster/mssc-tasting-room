package br.com.prcompany.mssctastingroom.repository;

import br.com.prcompany.mssctastingroom.domain.TastingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TastingRoomRepository extends JpaRepository<TastingRoom, UUID> {
}
