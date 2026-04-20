package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.domain.dto.Menu;
import nbcc.resto.mapper.MenuMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuRepositoryAdapter implements MenuRepository {

    private final MenuMapper menuMapper;
    private final MenuJPARepository menuJPARepository;

    public MenuRepositoryAdapter(MenuMapper menuMapper, MenuJPARepository menuJPARepository) {
        this.menuMapper = menuMapper;
        this.menuJPARepository = menuJPARepository;
    }

    @Override
    public List<Menu> getAll() {
        var entities = menuJPARepository.findAll();
        return menuMapper.toDTO(entities);
    }

    @Override
    public Optional<Menu> get(Long id) {
       var entity = menuJPARepository.findById(id);
       return menuMapper.toDTO(entity);
    }

    @Override
    public Menu create(Menu menu) {
        var entity = menuMapper.toEntity(menu);
        entity = menuJPARepository.save(entity);
        return menuMapper.toDTO(entity);
    }

    @Override
    public Menu update(Menu menu) throws ConcurrencyException {
        try{
            var entity = menuMapper.toEntity(menu);
            entity = menuJPARepository.save(entity);
            return menuMapper.toDTO(entity);
        }catch (ConcurrencyFailureException e){
            throw new ConcurrencyException(e);
        }
    }

    @Override
    public void delete(Long id) {
        menuJPARepository.deleteById(id);
    }

    @Override
    public boolean exists(String name) {
        return menuJPARepository.existsByNameIgnoreCase(name);
    }
}
