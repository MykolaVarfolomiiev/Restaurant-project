package nbcc.resto.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationGuestDetail {

    private final UUID uuid;
    private final String firstName;
    private final String lastName;
    private final int groupSize;
    private final ReservationRequestStatus status;
    private final String eventName;
    private final LocalDateTime seatingStartDateTime;

    public ReservationGuestDetail(UUID uuid, String firstName, String lastName, int groupSize,
                                  ReservationRequestStatus status, String eventName,
                                  LocalDateTime seatingStartDateTime) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupSize = groupSize;
        this.status = status;
        this.eventName = eventName;
        this.seatingStartDateTime = seatingStartDateTime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public ReservationRequestStatus getStatus() {
        return status;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDateTime getSeatingStartDateTime() {
        return seatingStartDateTime;
    }
}
