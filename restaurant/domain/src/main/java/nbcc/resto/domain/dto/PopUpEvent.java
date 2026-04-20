package nbcc.resto.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PopUpEvent {

    private Long id;

    @NotBlank
    private String name;

    private String eventDescription;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    @Positive
    private Integer durationInMinutes;

    @NotNull
    @PositiveOrZero
    private BigDecimal price;

    private Boolean isActive;

    private Boolean isArchived;

    private Long version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    private List<Seating> seatings;

    private Menu menu;

    public PopUpEvent() {
    }

    public PopUpEvent(PopUpEvent popUpEvent) {
        this(
                popUpEvent.getId(),
                popUpEvent.getName(),
                popUpEvent.getEventDescription(),
                popUpEvent.getStartDate(),
                popUpEvent.getEndDate(),
                popUpEvent.getDurationInMinutes(),
                popUpEvent.getPrice(),
                popUpEvent.getIsActive(),
                popUpEvent.getIsArchived(),
                popUpEvent.getVersion(),
                popUpEvent.getCreatedAt(),
                popUpEvent.getUpdatedAt(),
                popUpEvent.getMenu(),
                popUpEvent.getSeatings()
        );
    }

    public PopUpEvent(Long id, String name, String eventDescription, LocalDate startDate, LocalDate endDate,
                      Integer durationInMinutes, BigDecimal price, Boolean active, Boolean archived,  Long version,
                      LocalDateTime createdAt, LocalDateTime updatedAt, Menu menu, List<Seating> seatings) {
        this();
        this.id = id;
        this.name = name;
        this.eventDescription = eventDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationInMinutes = durationInMinutes;
        this.price = price;
        this.isActive = active;
        this.isArchived = archived;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.menu = menu;
        this.seatings = seatings;
    }

    public Long getId() {
        return id;
    }

    public PopUpEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PopUpEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public PopUpEvent setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public PopUpEvent setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PopUpEvent setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public PopUpEvent setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PopUpEvent setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public PopUpEvent setIsActive(Boolean active) {
        isActive = active;
        return this;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public PopUpEvent setIsArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public PopUpEvent setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PopUpEvent setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public PopUpEvent setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public List<Seating> getSeatings() {
        return seatings;
    }

    public PopUpEvent setSeatings(List<Seating> seatings) {
        this.seatings = seatings;
        return this;
    }

    public Menu getMenu() {
        return menu;
    }

    public PopUpEvent setMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public String getUpdatedAtFormatted() {
        return updatedAt == null ? "" : updatedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getCreatedAtFormatted() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getStartDateFormatted() {
        return startDate == null ? "" : startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getEndDateFormatted() {
        return endDate == null ? "" : endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
