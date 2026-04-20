package nbcc.resto.service;

import nbcc.common.result.ValidationResults;
import nbcc.resto.domain.dto.DiningTable;

import java.util.List;
import java.util.Optional;

public interface TableService {

    ValidationResults<List<DiningTable>> getAll();

    ValidationResults<DiningTable> get(Long id);

    ValidationResults<DiningTable> create(DiningTable table);

    ValidationResults<DiningTable> update(DiningTable table);

    ValidationResults<DiningTable> delete(Long id);
}
