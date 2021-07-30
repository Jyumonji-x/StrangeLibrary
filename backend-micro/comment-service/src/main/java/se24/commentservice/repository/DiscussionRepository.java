package se24.commentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se24.commentservice.domain.Discussion;
import se24.commentservice.service.DiscussionService;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion,Integer> {
    Discussion findDiscussionById(int id);

    List<Discussion> findDiscussionsByCommentId(int commentId);

    @Transactional
    void deleteDiscussionByIdAndUsername(int id ,String username);
}
