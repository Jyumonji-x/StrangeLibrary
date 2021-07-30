package se24.loggerservice.domain;

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
@Setter
@Getter
@ToString
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String copyId;
    private String username;
    private String operator;
    private String branch;
    private String category;
    private double price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeValid;
    private String note;
}
