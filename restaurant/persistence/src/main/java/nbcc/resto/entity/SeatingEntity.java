package nbcc.resto.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Seating")
public class SeatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    private String name;

    private LocalDateTime startDateTime;
    private int durationMinutes;
    private LocalDateTime createdDate;
    private LocalDateTime updatedAt;
    private boolean archived;

    @ElementCollection
    @CollectionTable(name = "seating_tables", joinColumns = @JoinColumn(name = "seating_id"))
    @Column(name = "table_id")
    private List<Long> tableIds = new ArrayList<>();

    public SeatingEntity() {
    }

    public Long getId() {
        return id;
    }

    public SeatingEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public SeatingEntity setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getName() {
        return name;
    }

    public SeatingEntity setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public SeatingEntity setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public SeatingEntity setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public SeatingEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    public SeatingEntity setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds != null ? new ArrayList<>(tableIds) : new ArrayList<>();
        return this;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SeatingEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isArchived() {
        return archived;
    }

    public SeatingEntity setArchived(boolean archived) {
        this.archived = archived;
        return this;
    }
    //need this for events
    public LocalDateTime getEndDateTime() {
        return startDateTime != null ? startDateTime.plusMinutes(durationMinutes) : null;

    }
}
