package nbcc.resto.repository;

import nbcc.resto.entity.SeatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatingJPARepository extends JpaRepository<SeatingEntity, Long> {
}
