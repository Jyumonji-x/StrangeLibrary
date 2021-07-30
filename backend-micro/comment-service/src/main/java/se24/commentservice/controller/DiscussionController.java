package se24.commentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se24.commentservice.controller.request.DiscussionDeleteRequest;
import se24.commentservice.controller.request.DiscussionRequest;
import se24.commentservice.repository.CommentRepository;
import se24.commentservice.repository.DiscussionRepository;
import se24.commentservice.service.CommentService;
import se24.commentservice.service.DiscussionService;
import se24.commentservice.tool.ReturnMap;

@RestController
public class DiscussionController {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final DiscussionService discussionService;
    private final DiscussionRepository discussionRepository;

    @Autowired
    public DiscussionController(CommentService commentService, CommentRepository commentRepository, DiscussionService discussionService, DiscussionRepository discussionRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.discussionService = discussionService;
        this.discussionRepository = discussionRepository;
    }

    // 讨论不是单独查看的，而是在查看评论区的时候打包发送

    // 用户发布讨论
    @PostMapping("/api/discussion/release")
    public ResponseEntity<?> releaseDiscussion(@RequestBody DiscussionRequest request,
                                               BindingResult result
    ) {
        System.out.println("发布讨论");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 发他丫的！
            map = discussionService.release(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    // 用户自己不满，删除讨论
    @PostMapping("/api/discussion/delete")
    public ResponseEntity<?> deleteDiscussion(@RequestBody DiscussionDeleteRequest request,
                                              BindingResult result
    ) {
        System.out.println("发布讨论");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 发他丫的！
            map = discussionService.delete(request);
        }
        return ResponseEntity.ok(map.getMap());
    }
}
