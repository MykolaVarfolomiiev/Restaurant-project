package nbcc.resto.repository;

import nbcc.common.exception.ConcurrencyException;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.mapper.PopUpEventMapper;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class PopUpEventRepositoryAdapter implements PopUpEventRepository {

    private final PopUpEventMapper popUpEventMapper;
    private final PopUpEventJPARepository popUpEventJPARepository;

    public PopUpEventRepositoryAdapter(PopUpEventMapper popUpEventMapper, PopUpEventJPARepository popUpEventJPARepository) {
        this.popUpEventMapper = popUpEventMapper;
        this.popUpEventJPARepository = popUpEventJPARepository;
    }

    @Override
    public List<PopUpEvent> getAll() {
        var entities = popUpEventJPARepository.findByIsArchivedFalse();
        return popUpEventMapper.toDTO(entities);
    }

    @Override
    public List<PopUpEvent> search(String name, LocalDate startDate, LocalDate endDate) {
        var entities = popUpEventJPARepository.search(name, startDate, endDate);
        return popUpEventMapper.toDTO(entities);
    }

    @Override
    public Optional<PopUpEvent> get(Long id) {
        var entity = popUpEventJPARepository.findByIdAndIsArchivedFalse(id);
        return popUpEventMapper.toDTO(entity);
    }

    @Override
    public PopUpEvent create(PopUpEvent popUpEvent) {
        var entity = popUpEventMapper.toEntity(popUpEvent);
        entity = popUpEventJPARepository.save(entity);
        return popUpEventMapper.toDTO(entity);
    }

    @Override
    public PopUpEvent update(PopUpEvent popUpEvent) throws ConcurrencyException {
       try{
//         //safer update
//           if (popUpEvent == null || popUpEvent.getId() == null) {
//               return null;
//           }
//
//           if (!popUpEventJPARepository.existsById(popUpEvent.getId())) {
//               return null;
//           }
           var entity = popUpEventMapper.toEntity(popUpEvent);
           entity = popUpEventJPARepository.save(entity);
           return popUpEventMapper.toDTO(entity);
       } catch (ConcurrencyFailureException e) {
           throw new ConcurrencyException(e);
       }
    }

    @Override
    public void delete(Long id) {
        //Archive when deleting an event if the end date is passed
        //Delete otherwise
        var entity = popUpEventJPARepository.findById(id).orElse(null);

        if (entity == null) {
            return;
        }

        if (entity.getEndDate() != null && entity.getEndDate().isBefore(LocalDate.now())) {
            entity.setIsArchived(true);
            entity.setIsActive(false);
            popUpEventJPARepository.save(entity);
            return;
        }
        popUpEventJPARepository.deleteById(id);
    }

    @Override
    public boolean exists(String name) {
        return popUpEventJPARepository.existsByNameIgnoreCase(name);
    }
}
