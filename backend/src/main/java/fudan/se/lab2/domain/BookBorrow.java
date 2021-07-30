package fudan.se.lab2.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fudan.se.lab2.domain.utils.JsonDateSerializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_borrow")
public class BookBorrow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "book_name")
    private String bookName;
    @Column(name = "user_name")
    private String username;
    @Column(name = "operator_name")
    private String operatorName;
    @Column(name = "duplicate_id")
    private String duplicateId;

    private String branch;
    private String category;

    private Date time;
    @Column(name = "time_valid")
    private Date timeValid;

    private double price;


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTimeValid() {
        return timeValid;
    }

    public void setTimeValid(Date timeValid) {
        this.timeValid = timeValid;
    }

    @Override
    public String toString() {
        return "BookBorrow{" +
                "id=" + id +
                ", book_name='" + bookName + '\'' +
                ", username='" + username + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", duplicateId='" + duplicateId + '\'' +
                ", branch='" + branch + '\'' +
                ", category='" + category + '\'' +
                ", time=" + time +
                ", timeValid=" + timeValid +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuplicateId() {
        return duplicateId;
    }

    public void setDuplicateId(String duplicate_id) {
        this.duplicateId = duplicate_id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operator_name) {
        this.operatorName = operator_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
