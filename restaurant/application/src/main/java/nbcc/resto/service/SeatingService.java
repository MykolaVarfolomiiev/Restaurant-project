package nbcc.resto.service;

import nbcc.common.result.ValidationResults;
import nbcc.resto.domain.dto.Seating;

import java.util.List;

public interface SeatingService {

    ValidationResults<List<Seating>> getAll();

    ValidationResults<Seating> get(Long id);

    ValidationResults<Seating> create(Seating seating);

    ValidationResults<Seating> update(Seating seating);

    ValidationResults<Seating> deleteOrArchive(Long id);
}
