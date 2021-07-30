package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author WangHaiWei
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByISBN(long ISBN);
    List<Book> findBooksByTitleLike(String title);
}