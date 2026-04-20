package nbcc.resto.domain.dto;

import java.time.LocalDateTime;

public class DiningTable {

    private Long id;
    private String name;
    private int capacity;
    private LocalDateTime createdDate;

    public DiningTable() {
    }

    public DiningTable(Long id, String name, int capacity) {
        this(id, name, capacity, LocalDateTime.now());
    }

    public DiningTable(Long id, String name, int capacity, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
