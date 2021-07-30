package se24.commentservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
//还书就立即创建一条记录，通过status判断是否已评论
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    protected String username;
    protected String isbn;
    protected double rate;
    protected String bookName;
    protected boolean hidden = false;//由管理员隐藏评论的方法
    protected String status = "未评论";//已评论、未评论
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date time;//评论、修改评论时间
    protected String title;//评论标题
    protected String comment;//评论本体
}
