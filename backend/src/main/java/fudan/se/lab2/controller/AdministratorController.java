package fudan.se.lab2.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.CreateAdminRequest;
import fudan.se.lab2.repository.BookRepository;
import fudan.se.lab2.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdministratorController {
    private final AdministratorService administratorService;

    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @PostMapping("/api/authority/create_admin")
    public ResponseEntity<?> createAdmin(
            @RequestBody CreateAdminRequest request
    ) throws JsonProcessingException {
        System.out.print("AuthorityController /api/authority/create_admin called:");
        return ResponseEntity.ok(administratorService.creatAdministrator(request).toJson());
    }


}
