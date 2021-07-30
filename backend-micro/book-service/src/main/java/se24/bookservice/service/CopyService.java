package se24.bookservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.bookservice.controller.request.CopyAddRequest;
import se24.bookservice.controller.request.UserReserveRequest;
import se24.bookservice.domain.Book;
import se24.bookservice.domain.Copy;
import se24.bookservice.domain.User;
import se24.bookservice.repository.BookRepository;
import se24.bookservice.repository.CopyRepository;
import se24.bookservice.util.ReturnMap;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class CopyService {
    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CopyService(CopyRepository copyRepository, BookRepository bookRepository, RestTemplate restTemplate) {
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * add new copy
     */

    public ReturnMap add(CopyAddRequest request) {
        // 检测超级管理员权限
        User admin = null;
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://localhost:9090/api/session/" + request.getSession(), null, User.class);
            admin = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("检验session接口调用失败，请检查user-service");
        }
        if (admin == null || !admin.isAdmin()) {
            ReturnMap map = new ReturnMap();
            map.setRtn(0);
            map.setMessage("创建管理员需要管理员权限");
            return map;
        }
        // 检测书本是否存在
        String isbn = request.getISBN();
        Book book = bookRepository.findBookByISBN(isbn);
        if (book == null) {
            ReturnMap map = new ReturnMap();
            map.setRtn(0);
            map.setMessage("添加失败，没有对应的书籍信息");
            return map;
        } else {
            return doAdd(request, book);
        }
    }

    private ReturnMap doAdd(CopyAddRequest request, Book book) {
        // 添加副本
        String isbn = request.getISBN();
        int count = copyRepository.countCopiesByISBN(isbn);
        for (int i = 1; i <= request.getNumber(); i++) {
            DecimalFormat decimalFormat = new DecimalFormat("000");
            String copyId = isbn + "-" + decimalFormat.format((long) count + i);
            // 默认状态为在库
            Copy copy = new Copy(book);
            copy.setCopyId(copyId);
            copy.setBranch(request.getLocation());
            copyRepository.save(copy);
        }
        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("副本入库成功,新增" + request.getNumber() + "本《" + book.getTitle() + "》");
        return map;
    }
    //许同樵 前端debug增
    public ReturnMap getBookCopies(String isbn){
            ReturnMap map = new ReturnMap();
            try {
                List<Copy> copies = copyRepository.findCopiesByISBN(isbn);
                map.setRtn(1);
                map.setMessage("查询副本成功");
                map.put("copies", copies);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                map.setRtn(0);
                map.setMessage("查询副本失败");
            }
            return map;
    }
    //许同樵 前端debug增
    public ReturnMap getUserReserved(UserReserveRequest request){
        ReturnMap map = new ReturnMap();
        try {
            List<Copy> copies = copyRepository.findCopiesByStatusAndBorrowerAndBranch("预约",request.getUsername(),request.getBranch());
            map.setRtn(1);
            map.setMessage("查询副本成功");
            map.put("copies", copies);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("查询副本失败");
        }
        return map;
    }
}
