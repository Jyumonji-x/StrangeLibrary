package se24.commentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se24.commentservice.controller.request.CommentRequest;
import se24.commentservice.controller.request.DeleteOrHideCommentRequest;
import se24.commentservice.domain.Comment;
import se24.commentservice.domain.Discussion;
import se24.commentservice.domain.pack.CommentPack;
import se24.commentservice.repository.CommentRepository;
import se24.commentservice.repository.DiscussionRepository;
import se24.commentservice.service.CommentService;
import se24.commentservice.tool.ReturnMap;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;

    @Autowired
    public CommentController(CommentService commentService, CommentRepository commentRepository, DiscussionRepository discussionRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.discussionRepository = discussionRepository;
    }

    @PutMapping("/api/comment/add")
    public ResponseEntity<?> comment(
            @RequestBody Comment comment
    ) {
        System.out.println("服务内调用，还书后添加一条评论（无论已评论或未评论都添加）");
        System.out.println(comment);
        return ResponseEntity.ok(commentService.add(comment).getMap());
    }

    @GetMapping("/api/comment/getBySession/{session}")
    public ResponseEntity<?> getCommentBySession(
            @PathVariable String session
    ) {
        System.out.println("通过用户session获取其所有评论，用于前端管理评论");
        System.out.println(session);
        return ResponseEntity.ok(commentService.getCommentsBySession(session).getMap());

    }

    @PostMapping("/api/comment/deleteComment")
    public ResponseEntity<?> deleteComment(
            @RequestBody DeleteOrHideCommentRequest request,
            BindingResult result
    ) {
        System.out.println("删除某个评论");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 删他丫的！
            map = commentService.deleteComment(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    @PostMapping("/api/comment/hideComment/")
    public ResponseEntity<?> hideComment(
            @RequestBody DeleteOrHideCommentRequest request,
            BindingResult result
    ) {
        System.out.println("隐藏某个评论");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 隐藏他丫的！
            map = commentService.hideComment(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    @PostMapping("/api/comment/reshowComment/")
    public ResponseEntity<?> reshowComment(
            @RequestBody DeleteOrHideCommentRequest request,
            BindingResult result
    ) {
        System.out.println("隐藏某个评论");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 重显他丫的！
            map = commentService.reshowComment(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    // 写评论
    @PostMapping("/api/comment/make")
    public ResponseEntity<?> makeComment(@RequestBody CommentRequest request,
                                         BindingResult result
    ) {
        System.out.println("写评论接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 评！
            map = commentService.makeComment(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    // 获取副本下方的评论，跟讨论打包一起发送
    @GetMapping("/api/comment/isbn/{isbn}")
    public ResponseEntity<?> getAll(
            @PathVariable String isbn
    ) {
        System.out.println("获取接口");
        System.out.println("isbn = " + isbn);
        List<Comment> comments = commentRepository.findCommentsByIsbn(isbn);
        List<CommentPack> packs = new ArrayList<>();
        if (comments != null){
            // 找到评论了！搜刮后面的东西
            for (Comment comment : comments) {
                List<Discussion> discussions = discussionRepository.findDiscussionsByCommentId(comment.getId());
                CommentPack pack = new CommentPack(comment);
                pack.setDiscussions(discussions);
                packs.add(pack);
            }
        }

        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("获取评论成功");
        map.put("comments", packs);
        return ResponseEntity.ok(map.getMap());
    }
}
