package nbcc.resto.domain.dto;

import java.time.LocalDateTime;

public class ReservationRequestSummary {

    private final Long id;
    private final Long seatingId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int groupSize;
    private final ReservationRequestStatus status;
    private final String uuid;
    private final LocalDateTime createdAt;
    private final String seatingName;
    private final LocalDateTime seatingStartDateTime;
    private final Long eventId;
    private final String eventName;

    public ReservationRequestSummary(Long id, Long seatingId, String firstName, String lastName, String email,
                                     int groupSize, ReservationRequestStatus status, String uuid, LocalDateTime createdAt,
                                     String seatingName, LocalDateTime seatingStartDateTime, Long eventId, String eventName) {
        this.id = id;
        this.seatingId = seatingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupSize = groupSize;
        this.status = status;
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.seatingName = seatingName;
        this.seatingStartDateTime = seatingStartDateTime;
        this.eventId = eventId;
        this.eventName = eventName;
    }

    public Long getId() {
        return id;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSeatingName() {
        return seatingName;
    }

    public LocalDateTime getSeatingStartDateTime() {
        return seatingStartDateTime;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getGuestFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}
