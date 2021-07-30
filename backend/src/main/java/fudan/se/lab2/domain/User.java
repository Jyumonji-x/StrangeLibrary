package fudan.se.lab2.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fudan.se.lab2.domain.utils.JsonDateSerializer;

import javax.persistence.*;
import java.util.*;

/**
 * @author LBW
 */
@Entity
public class User implements Cloneable {

    private static final long serialVersionUID = -6140085056226164016L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private int permission;
    private Date time_create;
    private Date time_login;
    private String identity;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", permission=" + permission +
                ", time_create=" + time_create +
                ", time_login=" + time_login +
                ", identity=" + identity +
                '}';
    }

    @Override
    public User clone() {
        return new User(this.username, this.password, this.email, this.permission, this.time_create, this.time_login, this.identity);
    }

    public User() {
    }

    public User(String username, String password, String email, int permission, Date time_create, Date time_login
            , String identity) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permission = permission;
        this.time_create = time_create;
        this.time_login = time_login;
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }


    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTime_create() {
        return time_create;
    }

    public void setTime_create(Date time_create) {
        this.time_create = time_create;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTime_login() {
        return time_login;
    }

    public void setTime_login(Date time_login) {
        this.time_login = time_login;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
