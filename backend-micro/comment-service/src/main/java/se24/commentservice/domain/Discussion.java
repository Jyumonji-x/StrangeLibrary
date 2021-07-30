package se24.commentservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    int commentId;
    String username;
    String isbn;
    String bookName;
    boolean hidden=false;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date time;//讨论发布时间
    String content;//评论本体
}
