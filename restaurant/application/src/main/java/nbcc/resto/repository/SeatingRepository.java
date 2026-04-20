package nbcc.resto.repository;

import nbcc.resto.domain.dto.Seating;

import java.util.List;
import java.util.Optional;

public interface SeatingRepository {

    List<Seating> getAll();

    Optional<Seating> get(Long id);

    Seating create(Seating seating);

    Seating update(Seating seating);

    void delete(Long id);
}
