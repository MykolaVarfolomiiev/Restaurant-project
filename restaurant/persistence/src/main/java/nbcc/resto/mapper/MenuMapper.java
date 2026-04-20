package nbcc.resto.mapper;

import nbcc.resto.domain.dto.Menu;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.entity.MenuEntity;
import nbcc.resto.entity.MenuItemEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper implements EntityMapper<Menu, MenuEntity> {

    @Override
    public Menu toDTO(MenuEntity entity) {

        if (entity == null) {
            return null;
        }

        var menu = new Menu()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setCreatedAt(entity.getCreatedAt())
                .setVersion(entity.getVersion())
                ;

        if (entity.getMenuItems() != null) {
            menu.setMenuItems(entity.getMenuItems().stream()
                    .map(this::toBasicMenuItem)
                    .toList());
        }
        return menu;
    }

    @Override
    public MenuEntity toEntity(Menu dto) {

        if (dto == null) {
            return null;
        }

        var menu =  new MenuEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setCreatedAt(dto.getCreatedAt())
                .setVersion(dto.getVersion())
                ;

        if (dto.getMenuItems() != null) {
            menu.setMenuItems(dto.getMenuItems().stream()
                    .map(menuItem -> toBasicMenuItemEntity(menuItem, menu))
                    .toList());
        }
        return menu;
    }

    private MenuItem toBasicMenuItem(MenuItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MenuItem()
                .setId(entity.getId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setVersion(entity.getVersion())
                .setMenu(new Menu().setId(entity.getMenu() != null ? entity.getMenu().getId() : null));
    }

    private MenuItemEntity toBasicMenuItemEntity(MenuItem dto, MenuEntity menu) {
        if (dto == null) {
            return null;
        }

        return new MenuItemEntity()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setVersion(dto.getVersion())
                .setMenu(menu);
    }
}
