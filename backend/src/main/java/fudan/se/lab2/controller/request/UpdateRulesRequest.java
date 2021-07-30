package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRulesRequest {
    private int maxAmountTeacher;
    private int maxAmountUndergra;
    private int maxAmountPostgra;

    private int borrowTimeTeacher;
    private int borrowTimeUndergra;
    private int borrowTimePostgra;

    private int subscribeTimeTeacher;
    private int subscribeTimeUndergra;
    private int subscribeTimePostgra;

    @Override
    public String toString() {
        return "UpdateRulesRequest{" +
                "maxAmountTeacher=" + maxAmountTeacher +
                ", maxAmountUndergra=" + maxAmountUndergra +
                ", maxAmountPostgra=" + maxAmountPostgra +
                ", borrowTimeTeacher=" + borrowTimeTeacher +
                ", borrowTimeUndergra=" + borrowTimeUndergra +
                ", borrowTimePostgra=" + borrowTimePostgra +
                ", subscribeTimeTeacher=" + subscribeTimeTeacher +
                ", subscribeTimeUndergra=" + subscribeTimeUndergra +
                ", subscribeTimePostgra=" + subscribeTimePostgra +
                '}';
    }

    public int getSubscribeTimePostgra() {
        return subscribeTimePostgra;
    }

    public void setSubscribeTimePostgra(int subscribeTimePostgra) {
        this.subscribeTimePostgra = subscribeTimePostgra;
    }

    public int getSubscribeTimeUndergra() {
        return subscribeTimeUndergra;
    }

    public void setSubscribeTimeUndergra(int subscribeTimeUndergra) {
        this.subscribeTimeUndergra = subscribeTimeUndergra;
    }

    public int getSubscribeTimeTeacher() {
        return subscribeTimeTeacher;
    }

    public void setSubscribeTimeTeacher(int subscribeTimeTeacher) {
        this.subscribeTimeTeacher = subscribeTimeTeacher;
    }

    public int getBorrowTimePostgra() {
        return borrowTimePostgra;
    }

    public void setBorrowTimePostgra(int borrowTimePostgra) {
        this.borrowTimePostgra = borrowTimePostgra;
    }

    public int getBorrowTimeUndergra() {
        return borrowTimeUndergra;
    }

    public void setBorrowTimeUndergra(int borrowTimeUndergra) {
        this.borrowTimeUndergra = borrowTimeUndergra;
    }

    public int getBorrowTimeTeacher() {
        return borrowTimeTeacher;
    }

    public void setBorrowTimeTeacher(int borrowTimeTeacher) {
        this.borrowTimeTeacher = borrowTimeTeacher;
    }

    public int getMaxAmountPostgra() {
        return maxAmountPostgra;
    }

    public void setMaxAmountPostgra(int maxAmountPostgra) {
        this.maxAmountPostgra = maxAmountPostgra;
    }

    public int getMaxAmountUndergra() {
        return maxAmountUndergra;
    }

    public void setMaxAmountUndergra(int maxAmountUndergra) {
        this.maxAmountUndergra = maxAmountUndergra;
    }

    public int getMaxAmountTeacher() {
        return maxAmountTeacher;
    }

    public void setMaxAmountTeacher(int maxAmountTeacher) {
        this.maxAmountTeacher = maxAmountTeacher;
    }
}
