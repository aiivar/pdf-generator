package ru.aivar.generatorpdfapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aivar.generatorpdfapp.models.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
