package fudan.se.lab2.service;

import fudan.se.lab2.domain.BookBorrow;
import fudan.se.lab2.domain.FineRecord;
import fudan.se.lab2.repository.BorrowRepository;
import fudan.se.lab2.repository.FineRepository;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FineService {
    private final FineRepository fineRepository;
    private final BorrowRepository borrowRepository;

    @Autowired
    public FineService(FineRepository fineRepository, BorrowRepository borrowRepository) {
        this.fineRepository = fineRepository;
        this.borrowRepository = borrowRepository;
    }

    public JsonMap getFine(String username) {
        List<FineRecord> fines = fineRepository.findFineRecordsByUsername(username);
        JsonMap map = new JsonMap();

        int rtn = 1;
        String message = "罚款查看成功";

        // 对所有罚金精确到小数点后两位向上取整
        double priceAll = getPriceAll(fines);

        map.put("rtn", rtn);
        map.put("message", message);
        map.put("fineRecords", fines);
        map.put("priceAll", priceAll);
        return map;
    }

    public JsonMap payFine(String username) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "付款成功";

        Date now = new Date();
        List<FineRecord> fines = fineRepository.findFineRecordsByUsername(username);
        double priceAll = getPriceAll(fines);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        Map<String, String> params = new HashMap<>();
        params.put("invoke_id", "se2021_1");
        params.put("uid", username);
        params.put("amount", "" + priceAll);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, httpHeaders);
        try {
            // 没有发生错误说明支付成功了！
            String response = restTemplate.postForObject("http://47.103.205.96:8080/api/payment", request, String.class);
            System.out.println("支付接口调用结果" + response);
            // 需要生成支付记录
            for (FineRecord fine : fines) {
                BookBorrow fine_history = new BookBorrow();
                fine_history.setBookName(fine.getBookName());
                fine_history.setCategory("支付罚款");
                fine_history.setDuplicateId(fine.getDuplicateId());
                fine_history.setTime(now);
                fine_history.setUsername(fine.getUsername());
                fine_history.setPrice(fine.getPrice());
                borrowRepository.save(fine_history);
            }
            // 清除罚单
            fineRepository.deleteAll(fines);
        } catch (Exception e) {
            // 捕获到异常说明余额不足支付失败
            System.out.println("调用payment接口时请求出错啦");
            System.out.println("exception:" + e.getMessage());
            // 需要生成支付失败记录
            for (FineRecord fine : fines) {
                BookBorrow fine_history = new BookBorrow();
                fine_history.setBookName(fine.getBookName());
                fine_history.setCategory("支付失败");
                fine_history.setDuplicateId(fine.getDuplicateId());
                fine_history.setTime(now);
                fine_history.setUsername(fine.getUsername());
                fine_history.setPrice(fine.getPrice());
                borrowRepository.save(fine_history);
            }
            rtn = 0;
            message = "余额不足，请充值后重试";
        }
        map.put("rtn", rtn);
        map.put("message", message);
        return map;
    }

    // 将罚金全部精确到小数点后两位 并计算总金额
    public double getPriceAll(List<FineRecord> fines) {
        double priceAll = 0;
        for (FineRecord fine : fines) {
            double price = fine.getPrice();
            BigDecimal priceBD = BigDecimal.valueOf(price);
            price = priceBD.setScale(2, RoundingMode.CEILING).doubleValue();
            priceAll += price;
            fine.setPrice(price);
        }
        BigDecimal priceBD = BigDecimal.valueOf(priceAll);
        return priceBD.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
