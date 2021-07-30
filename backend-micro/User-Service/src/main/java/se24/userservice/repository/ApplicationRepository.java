package se24.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se24.userservice.domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,String> {
    boolean existsApplicationByUsername(String username);

    @Transactional
    void deleteApplicationByUsername(String username);
}
