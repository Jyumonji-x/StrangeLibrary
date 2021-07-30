package se24.violationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se24.violationservice.domain.ReserveOverdue;
import se24.violationservice.repository.OverdueRepository;
import se24.violationservice.tool.ReturnMap;

import java.util.List;

@Service
public class OverdueService {
    private final OverdueRepository overdueRepository;

    @Autowired
    public OverdueService(OverdueRepository overdueRepository) {this.overdueRepository = overdueRepository;}

    public ReturnMap getOverdue(String username) {
        ReturnMap map = new ReturnMap();
        String message = "查看逾期预约成功";
        List<ReserveOverdue> overdueList = overdueRepository.findReserveOverduesByUsername(username);
        map.setRtn(1);
        map.setMessage(message);
        map.put("overdueList",overdueList);
        return map;
    }

    public ReturnMap deleteOverdue(String username) {
        ReturnMap map = new ReturnMap();
        int rtn = 1;
        String message = "确认逾期预约成功";
        try {
            overdueRepository.deleteReserveOverduesByUsername(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rtn = 0;
            message = "确认预期失败，请重试";
        }
        map.put("rtn",rtn);
        map.put("message",message);
        return map;
    }

}
