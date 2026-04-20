package nbcc.resto.mapper;

import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.entity.ReservationRequestEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReservationRequestMapper implements EntityMapper<ReservationRequest, ReservationRequestEntity> {

    @Override
    public ReservationRequest toDTO(ReservationRequestEntity entity) {
        if (entity == null) {
            return null;
        }
        UUID uuid = entity.getUuid() != null ? UUID.fromString(entity.getUuid()) : null;
        return new ReservationRequest(
                entity.getId(),
                entity.getSeatingId(),
                entity.getTableId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getGroupSize(),
                entity.getStatus(),
                uuid,
                entity.getCreatedAt()
        );
    }

    @Override
    public ReservationRequestEntity toEntity(ReservationRequest dto) {
        if (dto == null) {
            return null;
        }
        return new ReservationRequestEntity()
                .setId(dto.getId())
                .setSeatingId(dto.getSeatingId())
                .setTableId(dto.getTableId())
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail())
                .setGroupSize(dto.getGroupSize())
                .setStatus(dto.getStatus() != null ? dto.getStatus() : ReservationRequestStatus.PENDING)
                .setUuid(dto.getUuid() != null ? dto.getUuid().toString() : null)
                .setCreatedAt(dto.getCreatedAt());
    }
}
