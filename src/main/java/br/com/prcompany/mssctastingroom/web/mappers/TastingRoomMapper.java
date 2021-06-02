package br.com.prcompany.mssctastingroom.web.mappers;

import br.com.prcompany.mssctastingroom.domain.TastingRoom;
import br.com.prcompany.mssctastingroom.web.model.TastingRoomDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface TastingRoomMapper {

    TastingRoomDTO tastingRoomToDTO(TastingRoom tastingRoom);

    TastingRoom tastingRoomDTOToEntity(TastingRoomDTO tastingRoomDTO);
}
