package se24.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se24.bookservice.domain.Copy;

import java.util.List;

@Repository
public interface CopyRepository extends JpaRepository<Copy, String> {
    Copy findCopyByCopyId(String copyId);

    List<Copy> findCopiesByISBN(String ISBN);

    int countCopiesByISBN(String ISBN);

    int countCopiesByISBNAndStatus(String ISBN, String status);

    List<Copy> findCopiesByStatusAndBorrowerAndBranch(String status,String borrower,String branch);
}
