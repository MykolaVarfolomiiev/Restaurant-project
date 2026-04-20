package nbcc.resto.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class MenuItem {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Long version;
    @JsonIgnore
    private Menu menu;

    public MenuItem() {
    }

    public MenuItem(Menu menu) {
        this.menu = menu;
    }

    public MenuItem(Long id, String name, String description, Long version, Menu menu) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public MenuItem setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MenuItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MenuItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public MenuItem setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Long getMenuId() {
        return getMenu() != null ? getMenu().getId() : null;
    }

    @JsonIgnore
    public Menu getMenu() {
        return menu;
    }

    public MenuItem setMenu(Menu menu) {
        this.menu = menu;
        return this;
    }
}
