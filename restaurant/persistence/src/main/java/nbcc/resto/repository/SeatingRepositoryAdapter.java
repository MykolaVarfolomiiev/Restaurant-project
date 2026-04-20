package nbcc.resto.repository;

import nbcc.resto.domain.dto.Seating;
import nbcc.resto.mapper.SeatingMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatingRepositoryAdapter implements SeatingRepository {

    private final SeatingMapper seatingMapper;
    private final SeatingJPARepository seatingJPARepository;

    public SeatingRepositoryAdapter(SeatingMapper seatingMapper, SeatingJPARepository seatingJPARepository) {
        this.seatingMapper = seatingMapper;
        this.seatingJPARepository = seatingJPARepository;
    }

    @Override
    public List<Seating> getAll() {
        var entities = seatingJPARepository.findAll();
        return seatingMapper.toDTO(entities);
    }

    @Override
    public Optional<Seating> get(Long id) {
        var entity = seatingJPARepository.findById(id);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public Seating create(Seating seating) {
        var entity = seatingMapper.toEntity(seating);
        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public Seating update(Seating seating) {
        var entity = seatingMapper.toEntity(seating);
        entity = seatingJPARepository.save(entity);
        return seatingMapper.toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        seatingJPARepository.deleteById(id);
    }
}
