package nbcc.resto.repository;

import nbcc.resto.domain.dto.DiningTable;
import nbcc.resto.mapper.DiningTableMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiningTableRepositoryAdapter implements DiningTableRepository {

    private final DiningTableMapper diningTableMapper;
    private final DiningTableJPARepository diningTableJPARepository;

    public DiningTableRepositoryAdapter(DiningTableMapper diningTableMapper, DiningTableJPARepository diningTableJPARepository) {
        this.diningTableMapper = diningTableMapper;
        this.diningTableJPARepository = diningTableJPARepository;
    }

    @Override
    public List<DiningTable> getAll() {
        var entities = diningTableJPARepository.findAll();
        return diningTableMapper.toDTO(entities);
    }

    @Override
    public Optional<DiningTable> get(Long id) {
        var entity = diningTableJPARepository.findById(id);
        return diningTableMapper.toDTO(entity);
    }

    @Override
    public DiningTable create(DiningTable diningTable) {
        var entity = diningTableMapper.toEntity(diningTable);
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName("Table");
        }
        entity = diningTableJPARepository.save(entity);
        if (diningTable.getName() == null || diningTable.getName().isBlank()) {
            entity.setName("Table " + entity.getId());
            entity = diningTableJPARepository.save(entity);
        }
        return diningTableMapper.toDTO(entity);
    }

    @Override
    public DiningTable update(DiningTable diningTable) {
        var entity = diningTableMapper.toEntity(diningTable);
        entity = diningTableJPARepository.save(entity);
        return diningTableMapper.toDTO(entity);
    }

    @Override
    public DiningTable delete(Long id) {
        var entity = diningTableJPARepository.findById(id).orElse(null);
        if (entity != null) {
            diningTableJPARepository.deleteById(id);
            return diningTableMapper.toDTO(entity);
        }
        return null;
    }
}
