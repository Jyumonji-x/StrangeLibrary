package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Book;
import fudan.se.lab2.domain.BookBorrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<BookBorrow, Long> {
    List<BookBorrow> findBookBorrowsByUsernameOrderByTimeDesc(String username);

    List<BookBorrow> findAllByOrderByTimeDesc();

    List<BookBorrow> findBookBorrowsByDuplicateIdOrderByTimeDesc(String duplicateId);
}
