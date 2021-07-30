package se24.violationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se24.violationservice.domain.Fine;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine,Integer> {
    List<Fine> findFinesByUsername(String username);
}
