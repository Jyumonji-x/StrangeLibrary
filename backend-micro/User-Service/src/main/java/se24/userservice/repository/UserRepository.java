package se24.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se24.userservice.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String username);

    boolean existsByUsername(String username);

}
