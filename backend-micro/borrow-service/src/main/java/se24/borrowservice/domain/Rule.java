package se24.borrowservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@ToString
public class Rule {
    @Id
    @Column(length = 10)
    private String identity;

    private Integer borrowMaxNum;
    private Integer borrowValidTime;
    private Integer reserveValidTime;
}
