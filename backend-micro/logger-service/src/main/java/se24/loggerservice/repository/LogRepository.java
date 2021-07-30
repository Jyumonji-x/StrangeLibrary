package se24.loggerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se24.loggerservice.domain.Log;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findLogsByOperatorOrderByTimeDesc(String operator);

    List<Log> findLogsByUsernameOrderByTimeDesc(String username);

    List<Log> findLogsByCopyIdOrderByTimeDesc(String copyId);

    List<Log> findAllByOrderByTimeDesc();
}
