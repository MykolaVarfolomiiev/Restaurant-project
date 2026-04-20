package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@EntityListeners(AuditingEntityListener.class)
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "menu_name", nullable = false)
    private String name;

    @Column(name = "menu_description")
    private String description;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    // One menu can have many menu items (bidirectional relationship, owning side)
    @OneToMany(mappedBy = "menu")
    private List<MenuItemEntity> menuItems = new ArrayList<>();

    // One menu can have many events (bidirectional relationship, owning side)
    @OneToMany(mappedBy = "menu")
    private List<PopUpEventEntity> events = new ArrayList<>();

    public MenuEntity() {
    }

    public Long getId() {
        return id;
    }

    public MenuEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MenuEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public List<MenuItemEntity> getMenuItems() {
        return menuItems;
    }

    public MenuEntity setMenuItems(List<MenuItemEntity> menuItems) {
        this.menuItems = menuItems;
        return this;
    }

    public List<PopUpEventEntity> getEvents() {
        return events;
    }

    public MenuEntity setEvents(List<PopUpEventEntity> events) {
        this.events = events;
        return this;
    }
}
