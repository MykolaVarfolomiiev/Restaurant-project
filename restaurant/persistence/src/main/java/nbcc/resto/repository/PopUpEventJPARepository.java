package nbcc.resto.repository;

import nbcc.resto.entity.PopUpEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PopUpEventJPARepository extends JpaRepository<PopUpEventEntity, Long> {
    boolean existsByNameIgnoreCase(String name);

    List<PopUpEventEntity> findByIsArchivedFalse();

    Optional<PopUpEventEntity> findByIdAndIsArchivedFalse(Long id);

    @Query("""
            SELECT e
            FROM PopUpEventEntity e
              WHERE NOT (e.isActive = false AND e.isArchived = true)
              AND (:name IS NULL OR TRIM(:name) = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:startDate IS NULL OR e.startDate >= :startDate)
              AND (:endDate IS NULL OR e.endDate <= :endDate)
            ORDER BY e.startDate ASC, e.name ASC
            """)
    List<PopUpEventEntity> search(
            @Param("name") String name,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
