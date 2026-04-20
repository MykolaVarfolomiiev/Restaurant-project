package nbcc.resto.mapper;

import nbcc.resto.domain.dto.DiningTable;
import nbcc.resto.entity.DiningTableEntity;
import org.springframework.stereotype.Component;

@Component
public class DiningTableMapper implements EntityMapper<DiningTable, DiningTableEntity> {

    public DiningTableMapper() {
    }

    @Override
    public DiningTable toDTO(DiningTableEntity entity) {
        if (entity == null) {
            return null;
        }
        return new DiningTable(
                entity.getId(),
                entity.getName(),
                entity.getCapacity(),
                entity.getCreatedDate()
        );
    }

    @Override
    public DiningTableEntity toEntity(DiningTable dto) {
        if (dto == null) {
            return null;
        }
        var entity = new DiningTableEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setCapacity(dto.getCapacity())
                .setCreatedDate(dto.getCreatedDate());
        return entity;
    }
}
