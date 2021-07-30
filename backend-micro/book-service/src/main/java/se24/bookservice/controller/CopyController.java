package se24.bookservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se24.bookservice.controller.request.CopyAddRequest;
import se24.bookservice.controller.request.UserReserveRequest;
import se24.bookservice.domain.Copy;
import se24.bookservice.repository.CopyRepository;
import se24.bookservice.service.CopyService;
import se24.bookservice.util.ReturnMap;

import java.util.List;

@RestController
public class CopyController {
    private final CopyService copyService;
    private final CopyRepository copyRepository;

    @Autowired
    public CopyController(CopyService copyService, CopyRepository copyRepository) {
        this.copyService = copyService;
        this.copyRepository = copyRepository;
    }

    // 仅更新副本状态
    @PutMapping("/api/book/copy/{copyId}/{status}")
    public ResponseEntity<?> updateCopyStatus(
            @PathVariable String copyId,
            @PathVariable String status
    ) {
        Copy copy = copyRepository.findCopyByCopyId(copyId);
        copy.setStatus(status);
        if(status.equals("在库")||status.equals("遗失")||status.equals("损坏")){
            copy.setBorrower(null);
        }
        copyRepository.save(copy);
        return ResponseEntity.ok("更新副本状态接口");
    }


    // 更新副本
    @PutMapping("/api/book/copy")
    public ResponseEntity<?> updateCopy(
            @RequestBody Copy copy
    ) {
        copyRepository.save(copy);
        return ResponseEntity.ok("更新副本接口");
    }

    @GetMapping("/api/book/copy/{copyId}")
    public ResponseEntity<?> getCopy(
            @PathVariable String copyId
    ) {
        Copy copy = copyRepository.findCopyByCopyId(copyId);
        return ResponseEntity.ok(copy);
    }

// 许同樵 前端debug增
    @GetMapping("/api/book/copy/getCopyByIsbn/{ISBN}")
    public ResponseEntity<?> getBookCopies(
            @PathVariable String ISBN) {
        System.out.println(",查看书籍所有副本接口");
        System.out.println("ISBN: "+ISBN);
        return ResponseEntity.ok(copyService.getBookCopies(ISBN).getMap());
    }
    @PostMapping("/api/book/copy/add")
    public ResponseEntity<?> addCopy(
            @RequestBody CopyAddRequest request,
            BindingResult result
    ) {
        System.out.println("书籍服务,添加副本接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 注册
            map = copyService.add(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    //许同樵 前端debug 增加
    @PostMapping("/api/book/copy/userReserved/")
    public ResponseEntity<?> userReserve(@RequestBody UserReserveRequest request,
                                         BindingResult result) {
        System.out.println("查看用户已预约副本接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 预约！
            map = copyService.getUserReserved(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

}
