package se24.violationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se24.violationservice.domain.ReserveOverdue;

import java.util.List;

@Repository
public interface OverdueRepository extends JpaRepository<ReserveOverdue,Integer> {
    List<ReserveOverdue> findReserveOverduesByUsername(String username);

    @Transactional
    void deleteReserveOverduesByUsername(String username);
}
