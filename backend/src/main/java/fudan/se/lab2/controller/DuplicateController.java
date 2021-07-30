package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.BookDuplicateAddRequest;
import fudan.se.lab2.controller.request.ISBNRequest;
import fudan.se.lab2.service.DuplicateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DuplicateController {
    private final DuplicateService duplicateService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public DuplicateController(DuplicateService duplicateService) {
        this.duplicateService = duplicateService;
    }

    @PostMapping("/api/book_duplicate/add")
    public ResponseEntity<?> addBookDuplicate(@RequestBody BookDuplicateAddRequest request) throws JsonProcessingException {
        System.out.print("BorrowController /api/book_duplicate/add:");
        System.out.println("Param: token = "+request.getToken());
        System.out.println(request);
        return ResponseEntity.ok(duplicateService.add(request).toJson());
    }

    @PostMapping("/api/book_duplicate/search")
    public ResponseEntity<?> searchBookDuplicate(@RequestBody ISBNRequest request) throws JsonProcessingException {
        System.out.print("BorrowController /api/book_duplicate/search:");
        System.out.println("Param: ISBN = " + request.getISBN());
        return ResponseEntity.ok(duplicateService.searchByISBN(request.getISBN()).toJson());
    }
}
