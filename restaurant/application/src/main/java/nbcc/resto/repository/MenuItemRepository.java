package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.domain.dto.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    List<MenuItem> getAll();

    List<MenuItem> getAll(Long menuId);

    Optional<MenuItem> get(Long id);

    MenuItem create(MenuItem menuItem);

    MenuItem update(MenuItem menuItem) throws ConcurrencyException;

    void delete(Long id);

    boolean exists(String name);
}
