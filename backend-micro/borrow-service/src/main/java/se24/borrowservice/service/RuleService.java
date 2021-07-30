package se24.borrowservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se24.borrowservice.repository.BorrowRepository;
import se24.borrowservice.repository.RuleRepository;

@Service
public class RuleService {
    private final BorrowRepository borrowRepository;
    private final RuleRepository repository;

    @Autowired
    public RuleService(BorrowRepository borrowRepository, RuleRepository repository) {
        this.borrowRepository = borrowRepository;
        this.repository = repository;
    }

}
