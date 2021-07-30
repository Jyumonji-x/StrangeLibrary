package fudan.se.lab2.repository;

import fudan.se.lab2.domain.BorrowRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<BorrowRule, Long> {
    BorrowRule findBorrowRuleByIdentity(String identity);
}
