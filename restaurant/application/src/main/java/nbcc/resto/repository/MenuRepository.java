package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.domain.dto.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {

    List<Menu> getAll();

    Optional<Menu> get(Long id);

    Menu create(Menu menu);

    Menu update(Menu menu) throws ConcurrencyException;

    void delete(Long id);

    boolean exists(String name);
}
