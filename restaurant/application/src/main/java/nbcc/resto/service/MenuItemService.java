package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.domain.dto.MenuItem;

import java.util.Collection;

public interface MenuItemService {

    Result<Collection<MenuItem>> getAll();

    ValidatedResult<Collection<MenuItem>> getAll(Long menuId);

    ValidatedResult<MenuItem> get(Long id);

    ValidatedResult<MenuItem> create(MenuItem menuItem);

    ValidatedResult<MenuItem> update(MenuItem menuItem);

    ValidatedResult<Void> delete(Long id);

    ValidatedResult<Void> unlink(Long id);
}
