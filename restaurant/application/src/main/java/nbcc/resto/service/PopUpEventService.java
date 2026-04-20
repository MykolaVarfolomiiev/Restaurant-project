package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.domain.dto.PopUpEvent;

import java.time.LocalDate;
import java.util.Collection;

public interface PopUpEventService {

    Result<Collection<PopUpEvent>> getAll();

    Result<Collection<PopUpEvent>> getAll(boolean loadSeatings, boolean loadMenu);

    Result<Collection<PopUpEvent>> search(String name, LocalDate startDate, LocalDate endDate);

    ValidatedResult<PopUpEvent> get(Long id);

    ValidatedResult<PopUpEvent> get(Long id, boolean loadSeatings, boolean loadMenu);

    ValidatedResult<PopUpEvent> create(PopUpEvent event);

    ValidatedResult<PopUpEvent> update(PopUpEvent event);

    ValidatedResult<Void> delete(Long id);
}
