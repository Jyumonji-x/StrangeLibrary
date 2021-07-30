package se24.violationservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Log {
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
