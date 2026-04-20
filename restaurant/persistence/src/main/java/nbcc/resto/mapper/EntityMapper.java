package nbcc.resto.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EntityMapper<DTO, ENTITY> {

    default List<DTO> toDTO(Collection<ENTITY> entities){
        return entities.stream().map(this::toDTO).toList();
    }

    default List<ENTITY> toEntity(Collection<DTO> dtos){
        return dtos.stream().map(this::toEntity).toList();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default Optional<DTO> toDTO(Optional<ENTITY> entity){
        return entity.map(this::toDTO);
    }

    DTO toDTO(ENTITY entity);

    ENTITY toEntity(DTO dto);
}
