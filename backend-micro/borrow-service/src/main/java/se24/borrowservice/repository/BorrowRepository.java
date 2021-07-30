package se24.borrowservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se24.borrowservice.domain.Borrow;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow,String> {
    Borrow findBorrowByCopyId(String copyId);

    boolean existsByCopyId(String copyId);

    boolean existsByCopyIdAndStatus(String copyId,String status);

    int countByBorrower(String borrower);

    @Transactional
    void deleteBorrowByCopyId(String copyId);

    List<Borrow> findBorrowsByValidTimeBeforeAndStatus(Date now, String status);

    List<Borrow> findBorrowsByBorrower(String borrower);
}
