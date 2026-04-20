package nbcc.resto.service;

import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.DiningTable;
import nbcc.resto.repository.DiningTableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {

    private final DiningTableRepository diningTableRepository;

    public TableServiceImpl(DiningTableRepository diningTableRepository) {
        this.diningTableRepository = diningTableRepository;
    }

    @Override
    public ValidationResults<List<DiningTable>> getAll() {
        var list = diningTableRepository.getAll();
        return ValidationResults.success(list != null ? list : new ArrayList<>());
    }

    @Override
    public ValidationResults<DiningTable> get(Long id) {
        var table = diningTableRepository.get(id);
        return ValidationResults.success(table);
    }

    @Override
    public ValidationResults<DiningTable> create(DiningTable table) {
        try {
            if (table.getCapacity() < 1) {
                return ValidationResults.invalid(table, List.of(new ValidationError("Capacity must be at least 1", "capacity")));
            }

            String name = (table.getName() == null || table.getName().isBlank()) ? null : table.getName().trim();
            var toCreate = new DiningTable(null, name, table.getCapacity(), LocalDateTime.now());
            DiningTable created = diningTableRepository.create(toCreate);

            return ValidationResults.success(created);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<DiningTable> update(DiningTable table) {
        try {
            if (table.getId() == null || diningTableRepository.get(table.getId()).isEmpty()) {
                return ValidationResults.error();
            }

            if (table.getCapacity() < 1) {
                return ValidationResults.invalid(table, List.of(new ValidationError("Capacity must be at least 1", "capacity")));
            }

            var existing = diningTableRepository.get(table.getId()).orElseThrow();
            String name = (table.getName() == null || table.getName().isBlank()) ? "Table " + table.getId() : table.getName().trim();
            existing.setName(name);
            existing.setCapacity(table.getCapacity());

            DiningTable updated = diningTableRepository.update(existing);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<DiningTable> delete(Long id) {
        try {
            DiningTable removed = diningTableRepository.delete(id);
            return ValidationResults.success(removed);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }
}
