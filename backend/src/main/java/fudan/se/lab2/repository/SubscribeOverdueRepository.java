package fudan.se.lab2.repository;

import fudan.se.lab2.domain.SubscribeOverdue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubscribeOverdueRepository extends JpaRepository<SubscribeOverdue, Long> {
    @Override
    void deleteAll(Iterable<? extends SubscribeOverdue> iterable);

    List<SubscribeOverdue> findSubscribeOverduesByUsername(String username);

    @Transactional
    void deleteSubscribeOverduesByUsername(String username);
}
