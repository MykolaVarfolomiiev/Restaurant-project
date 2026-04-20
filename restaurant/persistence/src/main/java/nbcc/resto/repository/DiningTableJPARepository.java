package nbcc.resto.repository;

import nbcc.resto.entity.DiningTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableJPARepository extends JpaRepository<DiningTableEntity, Long> {
}
