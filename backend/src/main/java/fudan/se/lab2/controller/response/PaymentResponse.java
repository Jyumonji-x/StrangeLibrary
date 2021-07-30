package fudan.se.lab2.controller.response;

public class PaymentResponse<T> {
    private T data;
    private String msg;

    public PaymentResponse() {
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    public PaymentResponse(T data, String msg) {
        this.data = data;
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
