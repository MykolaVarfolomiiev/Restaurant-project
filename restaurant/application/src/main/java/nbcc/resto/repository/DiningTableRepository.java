package nbcc.resto.repository;

import nbcc.resto.domain.dto.DiningTable;

import java.util.List;
import java.util.Optional;

public interface DiningTableRepository {

    List<DiningTable> getAll();

    Optional<DiningTable> get(Long id);

    DiningTable create(DiningTable diningTable);

    DiningTable update(DiningTable diningTable);

    DiningTable delete(Long id);
}
