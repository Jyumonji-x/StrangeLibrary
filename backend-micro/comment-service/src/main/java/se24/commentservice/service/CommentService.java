package se24.commentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.commentservice.controller.request.CommentRequest;
import se24.commentservice.controller.request.DeleteOrHideCommentRequest;
import se24.commentservice.domain.Comment;
import se24.commentservice.domain.Log;
import se24.commentservice.domain.User;
import se24.commentservice.repository.CommentRepository;
import se24.commentservice.tool.ReturnMap;

import java.util.Date;
import java.util.List;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CommentService(CommentRepository userRepository, RestTemplate restTemplate) {
        this.commentRepository = userRepository;
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

    public ReturnMap getCommentsBySession(String session) {
        ReturnMap map = new ReturnMap();
        User user = getUserBySession(session);
        if (user == null) {
            map.setMessage("查看评论需要登录");
            return map;
        }
        String username = user.getUsername();
        System.out.println("username = " + username);
        List<Comment> comments = commentRepository.findAllByUsername(username);
        map.setRtn(1);
        map.put("comments", comments);
        return map;
    }

    // 此处接收到的一定是新评论，许多字段是默认状态
    public ReturnMap add(Comment comment) {
        ReturnMap map = new ReturnMap();
        try {
            if (commentRepository.existsByUsernameAndIsbn(comment.getUsername(), comment.getIsbn())) {
                map.setMessage("评论已存在");
            } else {
                commentRepository.save(comment);
                map.setMessage("创建评论成功");
            }
            map.setRtn(1);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("创建评论失败");
        }
        return map;
    }

    // 删除评论，删了就没了，需要重新还书才能再评
    public ReturnMap deleteComment(DeleteOrHideCommentRequest request) {
        ReturnMap map = new ReturnMap();
        User user = getUserBySession(request.getSession());
        if (user == null) {
            map.setMessage("需要已登录用户");
            return map;
        }
        String username = user.getUsername();
        int id = request.getId();

        Comment comment=commentRepository.findCommentByIdAndUsername(id,username);
        commentRepository.deleteByIdAndUsername(id, username);

        // 发送系统记录
        Date now=new Date();
        Log log = new Log();
        log.setTitle(comment.getBookName());
        log.setUsername(comment.getUsername());
        log.setOperator(user.getUsername());
        log.setCategory("删除评论");
        log.setPrice(0);
        log.setTime(now);
        log.setNote("评论标题:" + comment.getTitle());
        map.setRtn(0);
        restTemplate.put("http://localhost:9099/api/logger/log", log);

        map.setRtn(1);
        map.setMessage("删除评论成功！");
        return map;
    }

    // 隐藏评论
    public ReturnMap hideComment(DeleteOrHideCommentRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        User user = getUserBySession(request.getSession());
        if (user == null || !user.isAdmin()) {
            map.setMessage("需要管理员权限");
            return map;
        }
        int id = request.getId();
        Comment comment = commentRepository.findCommentById(id);
        //TODO 这里应该还需要检测评论是否存在
        if (comment.isHidden()) {
            map.setRtn(0);
            map.setMessage("评论已经隐藏");
        }
        comment.setHidden(true);
        commentRepository.save(comment);
        // 发送系统记录
        Date now=new Date();
        Log log = new Log();
        log.setTitle(comment.getBookName());
        log.setUsername(comment.getUsername());
        log.setOperator(user.getUsername());
        log.setCategory("隐藏评论");
        log.setPrice(0);
        log.setTime(now);
        log.setNote("评论标题:" + comment.getTitle());
        map.setRtn(0);
        restTemplate.put("http://localhost:9099/api/logger/log", log);
        map.setRtn(1);
        map.setMessage("隐藏评论成功！");
        return map;
    }

    // 恢复评论
    public ReturnMap reshowComment(DeleteOrHideCommentRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        User user = getUserBySession(request.getSession());
        if (user == null || !user.isAdmin()) {
            map.setMessage("需要管理员权限");
            return map;
        }
        int id = request.getId();
        Comment comment = commentRepository.findCommentById(id);
        //TODO 这里应该还需要检测评论是否存在
        if (comment.isHidden()) {
            map.setMessage("评论已经可见");
        }
        comment.setHidden(false);
        commentRepository.save(comment);
        // 发送系统记录
        Date now=new Date();
        Log log = new Log();
        log.setTitle(comment.getBookName());
        log.setUsername(comment.getUsername());
        log.setOperator(user.getUsername());
        log.setCategory("恢复评论");
        log.setPrice(0);
        log.setTime(now);
        log.setNote("评论标题:" + comment.getTitle());
        map.setRtn(0);
        restTemplate.put("http://localhost:9099/api/logger/log", log);
        map.setRtn(1);
        map.setMessage("恢复评论成功！");
        return map;
    }

    // 写评论
    public ReturnMap makeComment(CommentRequest request) {
        ReturnMap map = new ReturnMap();
        Date now = new Date();
        Comment comment = commentRepository.findCommentById(request.getId());
        comment.setRate(request.getRate());
        comment.setTime(now);
        comment.setComment(request.getComment());
        comment.setStatus("已评论");
        comment.setTitle(request.getTitle());
        commentRepository.save(comment);
        // 发送系统记录
        Log log = new Log();
        log.setTitle(comment.getBookName());
        log.setUsername(comment.getUsername());
        log.setCategory("进行评论");
        log.setPrice(0);
        log.setTime(now);
        log.setNote("评论标题:" + comment.getTitle());
        restTemplate.put("http://localhost:9099/api/logger/log", log);
        map.setRtn(1);
        map.setMessage("评论成功");
        return map;
    }
}
