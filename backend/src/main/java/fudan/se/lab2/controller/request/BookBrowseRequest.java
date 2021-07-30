package fudan.se.lab2.controller.request;

/**
 * @author ZhangMing
 */
public class BookBrowseRequest {

    private int page;
    private int size;

    public BookBrowseRequest(){
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "BookSearchRequest{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}

