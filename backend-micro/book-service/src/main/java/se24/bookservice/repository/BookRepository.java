package se24.bookservice.repository;

import  se24.bookservice.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Book findBookByISBN(String ISBN);
    List<Book> findAll();
    List<Book> findBooksByTitleLike(String title);
}
