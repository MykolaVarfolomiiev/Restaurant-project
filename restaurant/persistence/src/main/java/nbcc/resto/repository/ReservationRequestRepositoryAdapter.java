package nbcc.resto.repository;

import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.ReservationRequestSummary;
import nbcc.resto.mapper.ReservationRequestMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRequestRepositoryAdapter implements ReservationRequestRepository {

    private final ReservationRequestMapper mapper;
    private final ReservationRequestJPARepository jpaRepository;

    public ReservationRequestRepositoryAdapter(ReservationRequestMapper mapper, ReservationRequestJPARepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReservationRequest create(ReservationRequest request) {
        var entity = mapper.toEntity(request);
        entity = jpaRepository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public long countBySeatingId(Long seatingId) {
        return jpaRepository.countBySeatingId(seatingId);
    }

    @Override
    public boolean existsBySeatingId(Long seatingId) {
        return jpaRepository.existsBySeatingId(seatingId);
    }

    @Override
    public Optional<ReservationRequest> get(Long id) {
        return mapper.toDTO(jpaRepository.findById(id));
    }

    @Override
    public Optional<ReservationRequest> getByUuid(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        return mapper.toDTO(jpaRepository.findByUuid(uuid.toString()));
    }

    @Override
    public ReservationRequest update(ReservationRequest request) {
        var entity = mapper.toEntity(request);
        entity = jpaRepository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public boolean isTableReservedForSeating(Long seatingId, Long tableId) {
        if (seatingId == null || tableId == null) {
            return false;
        }
        return jpaRepository.existsBySeatingIdAndTableIdAndStatus(seatingId, tableId, ReservationRequestStatus.APPROVED);
    }

    @Override
    public List<ReservationRequestSummary> findSummaries(Long eventId, ReservationRequestStatus status) {
        return jpaRepository.findSummaries(eventId, status);
    }
}
