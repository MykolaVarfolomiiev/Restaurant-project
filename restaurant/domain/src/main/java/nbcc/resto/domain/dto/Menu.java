package nbcc.resto.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Menu {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private Long version;
    @JsonIgnore
    private PopUpEvent event;

    private List<MenuItem> menuItems;

    public Menu() {
    }

    public Menu(PopUpEvent event) {
        this.event = event;
    }

    public Menu(Menu menu) {
        this(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getCreatedAt(),
                menu.getVersion(),
                menu.getPopUpEvent(),
                menu.getMenuItems()
        );
    }

    public Menu(Long id, String name, String description,
                LocalDateTime createdAt, Long version, PopUpEvent event, List<MenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.version = version;
        this.event = event;
        this.menuItems = menuItems;
    }

    public Long getId() {
        return id;
    }

    public Menu setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Menu setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Menu setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Menu setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public Menu setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Long getPopUpEventId() {
        return  getPopUpEvent() != null ? getPopUpEvent().getId() : null;
    }

    @JsonIgnore
    public PopUpEvent getPopUpEvent() {
        return event;
    }

    public Menu setPopUpEvent(PopUpEvent event) {
        this.event = event;
        return this;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public Menu setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        return this;
    }

    // Helper methods
    public String getCreatedAtFormatted() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}
