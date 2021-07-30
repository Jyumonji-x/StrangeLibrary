package se24.borrowservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@ToString
//还书就立即创建一条记录，通过status判断是否已评论
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String username;
    String isbn;
    double rate;
    String bookName;
    boolean hidden=false;//由管理员隐藏评论的方法
    String status="未评论";//已评论、未评论
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date time;//评论、修改评论时间
    String title;//评论标题
    String comment;//评论本体
}
