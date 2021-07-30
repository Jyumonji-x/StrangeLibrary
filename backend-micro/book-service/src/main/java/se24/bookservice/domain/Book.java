package se24.bookservice.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author WangHaiWei , ZhangMing
 */

@Entity
@Setter
@Getter
@ToString
public class Book {
    @Id
    @Column(length = 15)
    private String ISBN;

    private String title;
    private String author;
    @Column(length = 2000)
    private String intro;

    private double price;
    private String cover;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time_publish;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time_create;
}
