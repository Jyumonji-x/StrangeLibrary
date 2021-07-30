package fudan.se.lab2.domain;

import javax.persistence.*;

@Entity
@Table(name = "borrow_rule")
public class BorrowRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Override
    public String toString() {
        return "BorrowRule{" +
                "id=" + id +
                ", identity='" + identity + '\'' +
                ", maxBorrowNumber=" + maxBorrowNumber +
                ", borrowValidPeriod=" + borrowValidPeriod +
                ", subscribeValidPeriod=" + subscribeValidPeriod +
                '}';
    }

    private String identity;
    // 最大借书本数
    @Column(name = "max_borrow_number")
    private Integer maxBorrowNumber;

    // 最长借书时间 单位为天
    @Column(name = "borrow_valid_period")
    private Integer borrowValidPeriod;

    // 最长预约时间 单位为天
    @Column(name = "subscribe_valid_period")
    private Integer subscribeValidPeriod;

    public BorrowRule() {
    }

    public BorrowRule(String identity, int maxBorrowNumber, int borrowValidPeriod, int subscribeValidPeriod) {
        this.identity = identity;
        this.maxBorrowNumber = maxBorrowNumber;
        this.borrowValidPeriod = borrowValidPeriod;
        this.subscribeValidPeriod = subscribeValidPeriod;
    }

    public Integer getSubscribeValidPeriod() {
        return subscribeValidPeriod;
    }

    public void setSubscribeValidPeriod(Integer subscribeValidPeriod) {
        this.subscribeValidPeriod = subscribeValidPeriod;
    }


    public Integer getMaxBorrowNumber() {
        return maxBorrowNumber;
    }

    public void setMaxBorrowNumber(Integer maxBorrowNumber) {
        this.maxBorrowNumber = maxBorrowNumber;
    }

    public Integer getBorrowValidPeriod() {
        return borrowValidPeriod;
    }

    public void setBorrowValidPeriod(Integer borrowValidPeriod) {
        this.borrowValidPeriod = borrowValidPeriod;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
