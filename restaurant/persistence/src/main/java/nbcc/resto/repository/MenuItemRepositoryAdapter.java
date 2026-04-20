package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.entity.MenuEntity;
import nbcc.resto.mapper.MenuItemMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuItemRepositoryAdapter implements MenuItemRepository{

    private final MenuItemMapper menuItemMapper;
    private final MenuItemJPARepository menuItemJPARepository;
    private final MenuJPARepository menuJPARepository;

    public MenuItemRepositoryAdapter(MenuItemMapper menuItemMapper, MenuItemJPARepository menuItemJPARepository, MenuJPARepository menuJPARepository) {
        this.menuItemMapper = menuItemMapper;
        this.menuItemJPARepository = menuItemJPARepository;
        this.menuJPARepository = menuJPARepository;
    }

    @Override
    public List<MenuItem> getAll() {
        var entities = menuItemJPARepository.findAll();
        return menuItemMapper.toDTO(entities);
    }

    @Override
    public List<MenuItem> getAll(Long menuId) {
        var entities = menuItemJPARepository.findAllByMenuId(menuId);
        return menuItemMapper.toDTO(entities);
    }

    @Override
    public Optional<MenuItem> get(Long id) {
        var entity = menuItemJPARepository.findById(id);
        return menuItemMapper.toDTO(entity);
    }

    @Override
    public MenuItem create(MenuItem menuItem) {
        var entity = menuItemMapper.toEntity(menuItem);

        if (menuItem.getMenuId() != null) {
            MenuEntity menu = menuJPARepository.getReferenceById(menuItem.getMenuId());
            entity.setMenu(menu);
        }

        entity = menuItemJPARepository.save(entity);
        return menuItemMapper.toDTO(entity);
    }

    @Override
    public MenuItem update(MenuItem menuItem) throws ConcurrencyException {
        try {
        var entity = menuItemMapper.toEntity(menuItem);

            if (menuItem.getMenuId() != null) {
                MenuEntity menu = menuJPARepository.getReferenceById(menuItem.getMenuId());
                entity.setMenu(menu);
            }

        entity = menuItemJPARepository.save(entity);
        return menuItemMapper.toDTO(entity);
        } catch (ConcurrencyFailureException e) {
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public void delete(Long id) {
        menuItemJPARepository.deleteById(id);
    }

    @Override
    public boolean exists(String name) {
        return menuItemJPARepository.existsByNameIgnoreCase(name);
    }
}
