package nbcc.resto.mapper;

import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.entity.PopUpEventEntity;
import org.springframework.stereotype.Component;

@Component
public class PopUpEventMapper implements EntityMapper<PopUpEvent, PopUpEventEntity>{

    private final MenuMapper menuMapper;

    public PopUpEventMapper(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public PopUpEvent toDTO(PopUpEventEntity entity) {

       if(entity == null) {
           return null;
       }

       return new PopUpEvent()
               .setId(entity.getId())
               .setName(entity.getName())
               .setEventDescription(entity.getEventDescription())
               .setStartDate(entity.getStartDate())
               .setEndDate(entity.getEndDate())
               .setDurationInMinutes(entity.getDurationInMinutes())
               .setPrice(entity.getPrice())
               .setIsActive(entity.getIsActive())
               .setIsArchived(entity.getIsArchived())
               .setVersion(entity.getVersion())
               .setCreatedAt(entity.getCreatedAt())
               .setUpdatedAt(entity.getUpdatedAt())
               .setMenu(menuMapper.toDTO(entity.getMenu()))
               ;
    }

    @Override
    public PopUpEventEntity toEntity(PopUpEvent dto) {

       if(dto == null) {
           return null;
       }

        boolean archived = Boolean.TRUE.equals(dto.getIsArchived());
        boolean active = !archived && Boolean.TRUE.equals(dto.getIsActive());

       return new PopUpEventEntity()
               .setId(dto.getId())
               .setName(dto.getName())
               .setEventDescription(dto.getEventDescription())
               .setStartDate(dto.getStartDate())
               .setEndDate(dto.getEndDate())
               .setDurationInMinutes(dto.getDurationInMinutes())
               .setPrice(dto.getPrice())
               .setIsActive(active)
               .setIsArchived(Boolean.TRUE.equals(dto.getIsArchived()))
               .setVersion(dto.getVersion())
               .setMenu(menuMapper.toEntity(dto.getMenu()))
               ;
    }
}
