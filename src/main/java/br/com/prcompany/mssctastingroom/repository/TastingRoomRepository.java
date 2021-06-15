package br.com.prcompany.mssctastingroom.repository;

import br.com.prcompany.mssctastingroom.domain.TastingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TastingRoomRepository extends JpaRepository<TastingRoom, UUID> {
}
