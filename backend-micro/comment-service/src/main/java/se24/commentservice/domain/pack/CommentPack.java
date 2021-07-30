package se24.commentservice.domain.pack;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se24.commentservice.domain.Comment;
import se24.commentservice.domain.Discussion;

import java.util.List;

@Setter
@Getter
@ToString
public class CommentPack extends Comment {
    private List<Discussion> discussions;

    public CommentPack(Comment comment){
        this.id=comment.getId();
        this.username=comment.getUsername();
        this.isbn=comment.getIsbn();
        this.rate=comment.getRate();
        this.bookName=comment.getBookName();
        this.hidden=comment.isHidden();
        this.status=comment.getStatus();
        this.time=comment.getTime();
        this.title=comment.getTitle();
        this.comment=comment.getComment();
    }
}
