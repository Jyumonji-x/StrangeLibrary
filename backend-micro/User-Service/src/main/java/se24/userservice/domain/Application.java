package se24.userservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Setter
@Getter
@ToString
public class Application {
    @Id
    @Column(length = 20)
    String username;// 申请的用户
    String reason;// 写理由！
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date time;
}
