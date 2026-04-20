package nbcc.resto.entity;

import jakarta.persistence.*;
import nbcc.resto.domain.dto.ReservationRequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_request")
public class ReservationRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seating_id", nullable = false)
    private Long seatingId;

    @Column(name = "table_id")
    private Long tableId;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "group_size", nullable = false)
    private int groupSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationRequestStatus status = ReservationRequestStatus.PENDING;

    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    private String uuid;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ReservationRequestEntity() {
    }

    public Long getId() {
        return id;
    }

    public ReservationRequestEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public ReservationRequestEntity setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
        return this;
    }

    public Long getTableId() {
        return tableId;
    }

    public ReservationRequestEntity setTableId(Long tableId) {
        this.tableId = tableId;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ReservationRequestEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationRequestEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ReservationRequestEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public ReservationRequestEntity setGroupSize(int groupSize) {
        this.groupSize = groupSize;
        return this;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public ReservationRequestEntity setStatus(ReservationRequestStatus status) {
        this.status = status;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public ReservationRequestEntity setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ReservationRequestEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
