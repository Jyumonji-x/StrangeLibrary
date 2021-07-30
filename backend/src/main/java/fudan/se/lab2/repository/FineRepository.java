package fudan.se.lab2.repository;

import fudan.se.lab2.domain.FineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineRecord, Long> {
    List<FineRecord> findFineRecordsByUsername(String username);
}
