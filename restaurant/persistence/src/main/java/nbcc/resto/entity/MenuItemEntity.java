package nbcc.resto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "menu_items")
public class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "menu_item_name", nullable = false)
    private String name;

    @Column(name = "menu_item_description", nullable = false)
    private String description;

    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menuItem_menu"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MenuEntity menu;

    public MenuItemEntity() {

    }

    public MenuItemEntity(MenuEntity menu) {
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public MenuItemEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItemEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItemEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuItemEntity setVersion(Long version) {
        this.version = version;
        return this;
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public MenuItemEntity setMenu(MenuEntity menu) {
        this.menu = menu;
        return this;
    }
}
