package fudan.se.lab2.domain;

import javax.persistence.*;

@Entity
@Table(name = "fine_record")
public class FineRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "book_name")
    private String bookName;
    @Column(name = "user_name")
    private String username;
    @Column(name = "duplicate_id")
    private String duplicateId;
    @Column(name = "price")
    private double price;
    private String reason;

    public FineRecord() {
    }

    public FineRecord(double price) {
        this.price = price;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FineRecord{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", username='" + username + '\'' +
                ", duplicateId='" + duplicateId + '\'' +
                ", price=" + price +
                '}';
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
