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
@Getter
@Setter
@ToString
public class User {
    @Id
    @Column(length = 20)
    private String username;
    private String password;
    private String email;
    private String permission;
    private String identity;
    private Integer credit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time_create;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time_login;

    public boolean isAdmin() {
        return this.permission.equals("管理员") || this.permission.equals("超级管理员");
    }

    public boolean isSuperAdmin() {
        return this.permission.equals("超级管理员");
    }
}
