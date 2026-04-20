package nbcc.resto.mapper;

import nbcc.resto.domain.dto.Menu;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.entity.MenuEntity;
import nbcc.resto.entity.MenuItemEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper implements EntityMapper<MenuItem, MenuItemEntity>{

    @Override
    public MenuItem toDTO(MenuItemEntity entity) {

        if (entity == null) {
            return null;
        }

        return new MenuItem()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setVersion(entity.getVersion())
                .setMenu(toBasicMenu(entity.getMenu()))
                ;
    }

    @Override
    public MenuItemEntity toEntity(MenuItem dto) {

        if (dto == null) {
            return null;
        }

        return new MenuItemEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setVersion(dto.getVersion())
                ;
    }

    private Menu toBasicMenu(MenuEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Menu()
                .setId(entity.getId())
                .setName(entity.getName());
    }
}
