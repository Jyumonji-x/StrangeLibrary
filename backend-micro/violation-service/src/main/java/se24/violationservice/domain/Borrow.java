package se24.violationservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@ToString
public class Borrow {
    // 一个副本只能有一个状态
    @Id
    @Column(length = 20)
    private String copyId;

    private String borrower;
    private String title;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validTime;
}
