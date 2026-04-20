package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.domain.dto.Menu;

import java.util.Collection;

public interface MenuService {

    Result<Collection<Menu>> getAll();

    Result<Collection<Menu>> getAll(boolean loadMenuItems);

    ValidatedResult<Menu> get(Long id);

    ValidatedResult<Menu> get(Long id, boolean loadMenuItems);

    ValidatedResult<Menu> create(Menu menu);

    ValidatedResult<Menu> update(Menu menu);

    ValidatedResult<Void> delete(Long id);
}
