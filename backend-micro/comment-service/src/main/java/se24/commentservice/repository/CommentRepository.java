package se24.commentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se24.commentservice.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByUsername(String username);
    @Transactional
    void deleteByIdAndUsername(int id,String username);
    Comment findCommentById(int id);
    Comment findCommentByIdAndUsername(int id,String username);
    boolean existsByUsernameAndIsbn(String username,String isbn);

    boolean existsByIdAndStatus(int id,String status);

    List<Comment> findCommentsByIsbn(String isbn);
}
