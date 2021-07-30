package fudan.se.lab2.repository;

import fudan.se.lab2.domain.BookDuplicate;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DuplicateRepository extends JpaRepository<BookDuplicate, Long> {
    int countByISBN(long ISBN);

    int countBookDuplicatesByISBN(long ISBN);

    int countBookDuplicatesByISBNAndStatus(long ISBN, String status);

    BookDuplicate findBookDuplicateByDuplicateId(String duplicate_id);

    List<BookDuplicate> findBookDuplicatesByISBN(long ISBN);

    List<BookDuplicate> findBookDuplicatesByStatus(String Status);

    List<BookDuplicate> findBookDuplicatesByBorrower(String borrower);

    List<BookDuplicate> findBookDuplicatesByTimeValidBefore(Date now);

    List<BookDuplicate> findBookDuplicatesByTimeValidBeforeAndStatus(Date now, String status);

    int countBookDuplicatesByBorrowerAndStatus(String borrower, String status);

    int countBookDuplicatesByBorrower(String borrower);
}
