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
@Getter
@Setter
@ToString
public class ReserveOverdue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    String username;
    String bookName;
    String copyId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date time;
}
