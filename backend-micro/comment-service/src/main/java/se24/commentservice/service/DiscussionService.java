package se24.commentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.commentservice.controller.request.DiscussionDeleteRequest;
import se24.commentservice.controller.request.DiscussionRequest;
import se24.commentservice.domain.Comment;
import se24.commentservice.domain.Discussion;
import se24.commentservice.domain.Log;
import se24.commentservice.domain.User;
import se24.commentservice.repository.CommentRepository;
import se24.commentservice.repository.DiscussionRepository;
import se24.commentservice.tool.ReturnMap;

import java.util.Date;

@Service
public class DiscussionService {
    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public DiscussionService(CommentRepository commentRepository, DiscussionRepository discussionRepository, RestTemplate restTemplate) {
        this.commentRepository = commentRepository;
        this.discussionRepository = discussionRepository;
        this.restTemplate = restTemplate;
    }

    private User getUserBySession(String session) {
        User user = null;
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://localhost:9090/api/session/" + session, null, User.class);
            user = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("检验session接口调用失败，请检查user-service");
        }
        return user;
    }

    // 发布评论
    public ReturnMap release(DiscussionRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // session检测
        User user = getUserBySession(request.getSession());
        if (user == null) {
            map.setMessage("发布讨论需要登录");
            return map;
        }
        // commentId检测 必须存在且已评论
        if (commentRepository.existsByIdAndStatus(request.getCommentId(), "已评论")) {
            Comment comment = commentRepository.findCommentById(request.getCommentId());
            return doRelease(request, user, comment);
        }
        map.setMessage("没有对应的评论");
        map.setRtn(0);
        return map;
    }

    private ReturnMap doRelease(DiscussionRequest request, User user, Comment comment) {
        Date now = new Date();
        Discussion discussion = new Discussion();
        discussion.setTime(now);
        discussion.setUsername(user.getUsername());
        discussion.setContent(request.getContent());
        discussion.setIsbn(comment.getIsbn());
        discussion.setBookName(comment.getBookName());
        discussion.setCommentId(comment.getId());
        discussionRepository.save(discussion);
        // 系统记录
        Log log = new Log();
        log.setTime(now);
        log.setNote("讨论内容:" + request.getContent());
        log.setTitle(comment.getBookName());
        log.setPrice(0);
        log.setCategory("发布讨论");
        log.setUsername(user.getUsername());
        restTemplate.put("http://localhost:9099/api/logger/log", log);

        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("发布讨论成功");
        return map;
    }

    // 删除评论
    public ReturnMap delete(DiscussionDeleteRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // session检测
        User user = getUserBySession(request.getSession());
        if (user == null) {
            map.setMessage("删除讨论需要登录");
            return map;
        }
        // 检测讨论Id
        Discussion discussion = discussionRepository.findDiscussionById(request.getId());
        discussionRepository.deleteDiscussionByIdAndUsername(request.getId(), user.getUsername());


        // 留下云彩
        Date now = new Date();
        Log log = new Log();
        System.out.println(discussion);
        log.setTitle(discussion.getBookName());
        log.setUsername(discussion.getUsername());
        log.setOperator(user.getUsername());
        log.setCategory("删除讨论");
        log.setPrice(0);
        log.setTime(now);
        log.setNote("讨论内容:" + discussion.getContent());
        restTemplate.put("http://localhost:9099/api/logger/log", log);
        map.setMessage("讨论删除成功");
        map.setRtn(1);
        return map;
    }
}
