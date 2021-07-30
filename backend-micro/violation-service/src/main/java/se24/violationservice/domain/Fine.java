package se24.violationservice.domain;

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
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    String bookName;
    String username;
    String copyId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date time;
    double price;
    private String reason;
}
