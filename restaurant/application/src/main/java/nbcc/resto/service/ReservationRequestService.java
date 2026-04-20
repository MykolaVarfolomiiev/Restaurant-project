package nbcc.resto.service;

import nbcc.common.result.ValidationResults;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.ReservationGuestDetail;
import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.ReservationRequestSummary;
import nbcc.resto.domain.dto.Seating;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRequestService {

    ValidationResults<List<PopUpEvent>> getActiveEventsForReserve();

    ValidationResults<List<Seating>> getActiveSeatingsForReserve();

    ValidationResults<ReservationRequest> create(ReservationRequest request);

    ValidationResults<List<ReservationRequestSummary>> listForEmployee(Long eventId, ReservationRequestStatus status);

    //Service method to receive approved reservations
    ValidationResults<List<ReservationRequestSummary>> listApprovedForEmployee(Long eventId);

    Optional<ReservationRequest> get(Long id);

    Optional<ReservationGuestDetail> findGuestDetailByUuid(UUID uuid);

    ValidationResults<ReservationRequest> approve(Long id, Long tableId);

    ValidationResults<ReservationRequest> deny(Long id);
}
