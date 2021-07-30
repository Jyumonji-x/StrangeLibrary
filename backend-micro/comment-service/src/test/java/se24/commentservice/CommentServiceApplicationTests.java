package se24.commentservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.tokens.CommentToken;
import se24.commentservice.controller.request.CommentRequest;
import se24.commentservice.controller.request.DeleteOrHideCommentRequest;
import se24.commentservice.controller.request.DiscussionDeleteRequest;
import se24.commentservice.controller.request.DiscussionRequest;
import se24.commentservice.domain.Comment;
import se24.commentservice.domain.Discussion;
import se24.commentservice.domain.User;
import se24.commentservice.repository.CommentRepository;
import se24.commentservice.repository.DiscussionRepository;
import se24.commentservice.service.CommentService;
import se24.commentservice.service.DiscussionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CommentServiceApplicationTests {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private DiscussionRepository discussionRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CommentService commentService;
    @InjectMocks
    private DiscussionService discussionService;

    private final String mockSession = "123";
    private final String mockUsername = "zmgg";
    private final String mockComment = "我是帅哥";
    private final String mockISBN = "66666";
    private final int mockId = 18;
    @Test
    void getCommentsWithoutSession() {
        ResponseEntity<User> responseEntity = ResponseEntity.ok(null);
        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        HashMap<String, Object> map = commentService.getCommentsBySession(mockSession).getMap();
        assertEquals("查看评论需要登录",map.get("message"));
        System.out.println(commentService.getCommentsBySession(mockSession).getMap());

    }
    /**
     *get comments
     **/

    @Test
    void getCommentsWithSession() {
        User user = new User();
        user.setPermission("");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Comment comment = new Comment();
        comment.setComment(mockComment);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);

        Mockito.when(commentRepository.findAllByUsername(mockUsername)).thenReturn(comments);
        HashMap<String, Object> map = commentService.getCommentsBySession(mockSession).getMap();
        assertEquals(1,map.get("rtn"));
        System.out.println(commentService.getCommentsBySession(mockSession).getMap());

    }

    /**
     *add comments
     **/
    @Test
    void addCommentWhenCommentAlreadyExist() {
        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);

        Mockito.when(commentRepository.existsByUsernameAndIsbn(comment.getUsername(), comment.getIsbn()))
                .thenReturn(true);
        HashMap<String, Object> map = commentService.add(comment).getMap();
        assertEquals("评论已存在",map.get("message"));
        System.out.println(commentService.add(comment).getMap());

    }

    @Test
    void addCommentCorrectly() {
        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        Mockito.when(commentRepository.existsByUsernameAndIsbn(comment.getUsername(), comment.getIsbn()))
                .thenReturn(false);
        HashMap<String, Object> map = commentService.add(comment).getMap();
        assertEquals("创建评论成功",map.get("message"));
        System.out.println(commentService.add(comment).getMap());

    }

    /**
     *delete comments
     **/

    @Test
    void deleteCommentCorrectly() {
        DeleteOrHideCommentRequest request = new DeleteOrHideCommentRequest();

        User user = new User();
        user.setPermission("");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        request.setId(mockId);
        request.setSession(mockSession);

        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(commentRepository.findCommentByIdAndUsername(mockId,mockUsername))
                .thenReturn(comment);


        HashMap<String, Object> map = commentService.deleteComment(request).getMap();
        assertEquals("删除评论成功！",map.get("message"));
        System.out.println(commentService.deleteComment(request).getMap());


    }

    /**
     *hide comments
     **/
    @Test
    void hideCommentCorrectly() {
        DeleteOrHideCommentRequest request = new DeleteOrHideCommentRequest();

        User user = new User();
        user.setPermission("超级管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        request.setId(mockId);
        request.setSession(mockSession);

        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(commentRepository.findCommentById(mockId))
                .thenReturn(comment);


        HashMap<String, Object> map = commentService.hideComment(request).getMap();
        assertEquals("隐藏评论成功！",map.get("message"));
        System.out.println(commentService.hideComment(request).getMap());


    }

    @Test
    void hideCommentWithoutPermission() {
        DeleteOrHideCommentRequest request = new DeleteOrHideCommentRequest();

        User user = new User();
        user.setPermission("");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        request.setId(mockId);
        request.setSession(mockSession);

        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(commentRepository.findCommentById(mockId))
                .thenReturn(comment);


        HashMap<String, Object> map = commentService.hideComment(request).getMap();
        assertEquals("需要管理员权限",map.get("message"));
        System.out.println(commentService.hideComment(request).getMap());


    }

    /**
     *reshow comments
     **/
    @Test
    void reshowCommentWithoutPermission() {
        DeleteOrHideCommentRequest request = new DeleteOrHideCommentRequest();

        User user = new User();
        user.setPermission("");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        request.setId(mockId);
        request.setSession(mockSession);

        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(commentRepository.findCommentById(mockId))
                .thenReturn(comment);


        HashMap<String, Object> map = commentService.reshowComment(request).getMap();
        assertEquals("需要管理员权限",map.get("message"));
        System.out.println(commentService.reshowComment(request).getMap());


    }

    @Test
    void reshowCommentCorrectly() {
        DeleteOrHideCommentRequest request = new DeleteOrHideCommentRequest();

        User user = new User();
        user.setPermission("超级管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        request.setId(mockId);
        request.setSession(mockSession);

        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(commentRepository.findCommentById(mockId))
                .thenReturn(comment);


        HashMap<String, Object> map = commentService.reshowComment(request).getMap();
        assertEquals("恢复评论成功！",map.get("message"));
        System.out.println(commentService.reshowComment(request).getMap());


    }

    /**
     *make comments
     **/

    @Test
    void makeCommentCorrectly() {
        CommentRequest request = new CommentRequest();
        request.setId(mockId);

        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);

        Mockito.when( commentRepository.findCommentById(request.getId())).thenReturn(comment);
        HashMap<String, Object> map = commentService.makeComment(request).getMap();
        assertEquals("评论成功",map.get("message"));
        System.out.println(commentService.add(comment).getMap());
    }

    /**
     *release discussions
     **/
    @Test
    void releaseDiscussionCorrectly(){
        DiscussionRequest request = new DiscussionRequest();
        request.setSession(mockSession);
        Comment comment = new Comment();
        comment.setUsername(mockUsername);
        comment.setIsbn(mockISBN);
        comment.setComment(mockComment);
        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);
        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(commentRepository.existsByIdAndStatus(request.getCommentId(), "已评论")).thenReturn(true);
        Mockito.when(commentRepository.findCommentById(request.getCommentId())).thenReturn(comment);
        HashMap<String, Object> map = discussionService.release(request).getMap();
        assertEquals("发布讨论成功",map.get("message"));
        System.out.println(discussionService.release(request).getMap());
    }

    /**
     *delete discussions
     **/
    @Test
    void deleteDiscussionCorrectly(){
        DiscussionDeleteRequest request = new DiscussionDeleteRequest();
        request.setSession(mockSession);
        request.setId(1);
        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);
        Discussion discussion=new Discussion();
        discussion.setId(1);
        discussion.setUsername(mockUsername);
        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        HashMap<String, Object> map = discussionService.delete(request).getMap();
        assertEquals("讨论删除成功",map.get("message"));
        System.out.println(discussionService.delete(request).getMap());
    }
}