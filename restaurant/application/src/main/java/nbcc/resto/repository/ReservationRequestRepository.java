package nbcc.resto.repository;

import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.ReservationRequestSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRequestRepository {

    ReservationRequest create(ReservationRequest request);

    long countBySeatingId(Long seatingId);

    boolean existsBySeatingId(Long seatingId);

    Optional<ReservationRequest> get(Long id);

    Optional<ReservationRequest> getByUuid(UUID uuid);

    ReservationRequest update(ReservationRequest request);

    boolean isTableReservedForSeating(Long seatingId, Long tableId);

    List<ReservationRequestSummary> findSummaries(Long eventId, ReservationRequestStatus status);
}
