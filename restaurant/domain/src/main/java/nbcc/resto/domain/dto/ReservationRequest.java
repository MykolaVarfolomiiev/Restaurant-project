package nbcc.resto.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationRequest {

    private Long id;
    private Long seatingId;
    private Long tableId;
    private String firstName;
    private String lastName;
    private String email;
    private int groupSize;
    private ReservationRequestStatus status;
    private UUID uuid;
    private LocalDateTime createdAt;

    public ReservationRequest() {
    }

    public ReservationRequest(Long id, Long seatingId, Long tableId, String firstName, String lastName, String email,
                              int groupSize, ReservationRequestStatus status, UUID uuid, LocalDateTime createdAt) {
        this.id = id;
        this.seatingId = seatingId;
        this.tableId = tableId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupSize = groupSize;
        this.status = status;
        this.uuid = uuid;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public void setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationRequestStatus status) {
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
