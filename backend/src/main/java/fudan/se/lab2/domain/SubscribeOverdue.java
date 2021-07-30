package fudan.se.lab2.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscribe_overdue")
public class SubscribeOverdue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="book_name")
    private String bookName;
    @Column(name = "user_name")
    private String username;
    @Column(name = "duplicate_id")
    private String duplicateId;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDuplicateId() {
        return duplicateId;
    }

    public void setDuplicateId(String duplicateId) {
        this.duplicateId = duplicateId;
    }

    @Override
    public String toString() {
        return "SubscribeOverdue{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", username='" + username + '\'' +
                ", duplicateId='" + duplicateId + '\'' +
                '}';
    }
}
