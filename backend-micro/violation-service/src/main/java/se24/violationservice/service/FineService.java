package se24.violationservice.service;

import org.hibernate.loader.plan.build.internal.LoadGraphLoadPlanBuildingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.violationservice.domain.Fine;
import se24.violationservice.domain.Log;
import se24.violationservice.repository.FineRepository;
import se24.violationservice.tool.ReturnMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FineService {
    private final FineRepository fineRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public FineService(FineRepository fineRepository, RestTemplate restTemplate) {
        this.fineRepository = fineRepository;
        this.restTemplate = restTemplate;
    }

    public ReturnMap getFine(String username) {
        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("罚款查看成功");

        List<Fine> fines = fineRepository.findFinesByUsername(username);
        double priceAll = getPriceAll(fines);

        map.put("fineRecords", fines);
        map.put("priceAll", priceAll);
        return map;
    }

    public ReturnMap payFine(String username) {
        Date now = new Date();
        List<Fine> fines = fineRepository.findFinesByUsername(username);
        double priceAll = getPriceAll(fines);

        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        // 调用助教的支付接口

        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        Map<String, String> params = new HashMap<>();
        params.put("invoke_id", "se2021_1");
        params.put("uid", username);
        params.put("amount", "" + priceAll);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, httpHeaders);
        try {
            String response = restTemplate.postForObject("http://47.103.205.96:8080/api/payment", request, String.class);
            System.out.println("支付接口调用结果" + response);
            // 成功！ 生成支付记录
            for (Fine fine : fines) {
                Log log = new Log();
                log.setTitle(fine.getBookName());
                log.setCategory("支付罚款成功");
                log.setCopyId(fine.getCopyId());
                log.setTime(now);
                log.setUsername(fine.getUsername());
                log.setPrice(fine.getPrice());
                log.setNote("支付成功，恢复信用");
                restTemplate.put("http://localhost:9099/api/logger/log", log);
                map.setMessage("支付成功");
            }
            // 恢复用户信用
            restTemplate.put("http://localhost:9090/api/credit/" + username + "/" + "100", null);
            // 清除罚单
            fineRepository.deleteAll(fines);
        } catch (Exception e) {
            for (Fine fine : fines) {
                Log log = new Log();
                log.setTitle(fine.getBookName());
                log.setCategory("支付罚款失败");
                log.setCopyId(fine.getCopyId());
                log.setTime(now);
                log.setUsername(fine.getUsername());
                log.setPrice(fine.getPrice());
                restTemplate.put("http://localhost:9099/api/logger/log", log);
                map.setMessage("支付失败，余额不足");
            }
        }
        return map;
    }

    // 将罚金全部精确到小数点后两位 并计算总金额
    public double getPriceAll(List<Fine> fines) {
        double priceAll = 0;
        for (Fine fine : fines) {
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
