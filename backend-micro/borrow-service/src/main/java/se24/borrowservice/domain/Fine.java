package se24.borrowservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@ToString
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    String bookName;
    String username;
    String copyId;
    double price;
    private String reason;
}
