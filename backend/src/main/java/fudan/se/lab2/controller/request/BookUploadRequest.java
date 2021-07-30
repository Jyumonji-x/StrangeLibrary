package fudan.se.lab2.controller.request;


import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

/**
 * @author WangHaiWei
 */
public class BookUploadRequest {
    private String title;
    private String author;
    private String intro;
    private long ISBN;
    private MultipartFile cover;
    private Date time_publish;


    public BookUploadRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public MultipartFile getCover() {
        return cover;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }

    public Date getTime_publish() {
        return time_publish;
    }

    public void setTime_publish(Date time_publish) {
        this.time_publish = time_publish;
    }

    @Override
    public String toString() {
        return "BookUploadRequest{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", intro='" + intro + '\'' +
                ", ISBN=" + ISBN +
                ", cover='" + cover.getName() + '\'' +
                ", time_publish=" + time_publish +
                '}';
    }
}

