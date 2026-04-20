package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class PopUpEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "event_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String eventDescription;

    @NotNull
    @Column(name = "start_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    @Column(name = "duration_in_minutes", nullable = false)
    private Integer durationInMinutes;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived = false;

    @CreatedDate
    @Column(name = "created", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_events_menu"))
    private MenuEntity menu;

    public PopUpEventEntity() {

    }

    public Long getId() {
        return id;
    }

    public PopUpEventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PopUpEventEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public PopUpEventEntity setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public PopUpEventEntity setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PopUpEventEntity setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public PopUpEventEntity setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PopUpEventEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public PopUpEventEntity setIsActive(Boolean active) {
        isActive = active;
        return this;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public PopUpEventEntity setIsArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PopUpEventEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PopUpEventEntity setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public PopUpEventEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public PopUpEventEntity setMenu(MenuEntity menu) {
        this.menu = menu;
        return this;
    }
}
