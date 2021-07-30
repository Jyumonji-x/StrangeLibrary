package fudan.se.lab2.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fudan.se.lab2.domain.utils.JsonDateSerializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_duplicate")
public class BookDuplicate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(name = "duplicate_id", unique = true)
    private String duplicateId;
    private String branch;
    private long ISBN;
    @Column(name = "time_create")
    private Date timeCreate;
    // 状态
    private String status;
    // 被预约or借出状态下才有的属性
    private String borrower;
    private Date timeStart;// 被预约或被借走的时间
    private Date timeValid;// 预约或借书的到期时间



    @Override
    public String toString() {
        return "BookDuplicate{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duplicateId='" + duplicateId + '\'' +
                ", branch='" + branch + '\'' +
                ", ISBN=" + ISBN +
                ", timeCreate=" + timeCreate +
                ", status=" + status +
                ", borrower='" + borrower + '\'' +
                ", timeStart=" + timeStart +
                ", timeValid=" + timeValid +
                '}';
    }

    public BookDuplicate() {
    }

    public BookDuplicate(String title, Long ISBN, String duplicateId, Date timeCreate, String branch) {
        this.title = title;
        this.ISBN = ISBN;
        this.duplicateId = duplicateId;
        this.status = "在库";
        this.branch = branch;
        this.timeCreate = timeCreate;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTimeCreate() {
        return timeCreate;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTimeStart() {
        return timeStart;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTimeValid() {
        return this.timeValid;
    }

    public void setTimeCreate(Date time_create) {
        this.timeCreate = time_create;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeValid(Date timeValid) {
        this.timeValid = timeValid;
    }
}
