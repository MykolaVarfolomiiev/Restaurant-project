package nbcc.resto.repository;

import nbcc.resto.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuJPARepository extends JpaRepository<MenuEntity, Long> {

    boolean existsByNameIgnoreCase(String name);
}
