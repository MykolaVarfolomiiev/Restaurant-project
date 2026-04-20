package nbcc.resto.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Seating {

    private Long id;
    private Long eventId;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm", fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss"})
    private LocalDateTime startDateTime;
    private int durationMinutes;
    private LocalDateTime createdDate;
    private LocalDateTime updatedAt;
    private boolean archived;
    private List<Long> tableIds = new ArrayList<>();

    public Seating() {
    }

    public Seating(Long id, Long eventId, String name, LocalDateTime startDateTime, int durationMinutes,
                   LocalDateTime createdDate, LocalDateTime updatedAt, boolean archived, List<Long> tableIds) {
        this.id = id;
        this.eventId = eventId;
        this.name = name;
        this.startDateTime = startDateTime;
        this.durationMinutes = durationMinutes;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.updatedAt = updatedAt;
        this.archived = archived;
        this.tableIds = tableIds != null ? new ArrayList<>(tableIds) : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds != null ? new ArrayList<>(tableIds) : new ArrayList<>();
    }

    /** End time = start + duration. Used for overlap checks. */
    public LocalDateTime getEndDateTime() {
        return startDateTime != null ? startDateTime.plusMinutes(durationMinutes) : null;
    }
}
