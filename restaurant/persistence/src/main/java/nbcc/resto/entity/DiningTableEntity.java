package nbcc.resto.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DinningTable")
public class DiningTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TableName")
    private String name;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "LocalDateTime")
    private LocalDateTime createdDate;

    public DiningTableEntity() {
    }

    public Long getId() {
        return id;
    }

    public DiningTableEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DiningTableEntity setName(String name) {
        this.name = name;
        return this;
    }

    public int getCapacity() {
        return capacity;
    }

    public DiningTableEntity setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public DiningTableEntity setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
