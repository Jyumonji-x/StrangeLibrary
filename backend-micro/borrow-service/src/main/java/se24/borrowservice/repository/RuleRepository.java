package se24.borrowservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se24.borrowservice.domain.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, String> {
    Rule findRuleByIdentity(String identity);

    boolean existsByIdentity(String identity);
}
