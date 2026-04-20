package nbcc.resto.controller.api;

/**
 * so the api only accepts fields the client is allowed to send  and our domain 
 * doesnt include the enventId but it is neeeded
 */
public class ReservationRequestCreateBody {

    private Long eventId;
    private Long seatingId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer groupSize;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getSeatingId() {
        return seatingId;
    }

    public void setSeatingId(Long seatingId) {
        this.seatingId = seatingId;
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

    public Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(Integer groupSize) {
        this.groupSize = groupSize;
    }
}
