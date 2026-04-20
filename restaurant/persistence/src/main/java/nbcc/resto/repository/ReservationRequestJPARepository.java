package nbcc.resto.repository;

import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.ReservationRequestSummary;
import nbcc.resto.entity.ReservationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRequestJPARepository extends JpaRepository<ReservationRequestEntity, Long> {

    long countBySeatingId(Long seatingId);

    boolean existsBySeatingId(Long seatingId);

    Optional<ReservationRequestEntity> findByUuid(String uuid);

    boolean existsBySeatingIdAndTableIdAndStatus(Long seatingId, Long tableId, ReservationRequestStatus status);

    @Query("""
            SELECT new nbcc.resto.domain.dto.ReservationRequestSummary(
                rr.id, rr.seatingId, rr.firstName, rr.lastName, rr.email, rr.groupSize, rr.status,
                rr.uuid, rr.createdAt, s.name, s.startDateTime, s.eventId, e.name)
            FROM ReservationRequestEntity rr, SeatingEntity s, PopUpEventEntity e
            WHERE rr.seatingId = s.id AND s.eventId = e.id
            AND s.archived = false
            AND e.isArchived = false
            AND (:eventId IS NULL OR s.eventId = :eventId)
            AND (:status IS NULL OR rr.status = :status)
            ORDER BY s.startDateTime ASC
            """)
    List<ReservationRequestSummary> findSummaries(@Param("eventId") Long eventId, @Param("status") ReservationRequestStatus status);
}
