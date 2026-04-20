package nbcc.resto.mapper;

import nbcc.resto.domain.dto.Seating;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatingMapper implements EntityMapper<Seating, SeatingEntity> {

    public SeatingMapper() {
    }

    @Override
    public Seating toDTO(SeatingEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Seating(
                entity.getId(),
                entity.getEventId(),
                entity.getName(),
                entity.getStartDateTime(),
                entity.getDurationMinutes(),
                entity.getCreatedDate(),
                entity.getUpdatedAt(),
                entity.isArchived(),
                entity.getTableIds()
        );
    }

    @Override
    public SeatingEntity toEntity(Seating dto) {
        if (dto == null) {
            return null;
        }
        return new SeatingEntity()
                .setId(dto.getId())
                .setEventId(dto.getEventId())
                .setName(dto.getName())
                .setStartDateTime(dto.getStartDateTime())
                .setDurationMinutes(dto.getDurationMinutes())
                .setCreatedDate(dto.getCreatedDate())
                .setUpdatedAt(dto.getUpdatedAt())
                .setArchived(dto.isArchived())
                .setTableIds(dto.getTableIds());
    }
}
