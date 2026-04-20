package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.domain.dto.PopUpEvent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PopUpEventRepository {

    List<PopUpEvent> getAll();

    List<PopUpEvent> search(String name, LocalDate startDate, LocalDate endDate);

    Optional<PopUpEvent> get(Long id);

    PopUpEvent create(PopUpEvent popUpEvent);

    PopUpEvent update(PopUpEvent popUpEvent) throws ConcurrencyException;

    void delete(Long id);

    boolean exists(String name);
}
