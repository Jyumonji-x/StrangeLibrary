package se24.loggerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se24.loggerservice.domain.Log;
import se24.loggerservice.repository.LogRepository;
import se24.loggerservice.tool.ReturnMap;

import java.util.List;

@Service
public class LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public ReturnMap add(Log log) {
        ReturnMap map = new ReturnMap();
        try {
            logRepository.save(log);
            map.setRtn(1);
            map.setMessage("创建记录成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("创建记录失败");
        }
        return map;
    }

    public ReturnMap getByUsername(String username) {
        ReturnMap map = new ReturnMap();
        try {
            List<Log> logs = logRepository.findLogsByUsernameOrderByTimeDesc(username);
            map.setRtn(1);
            map.setMessage("记录查询成功");
            map.put("logs", logs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("记录查询失败");
        }
        return map;
    }

    public ReturnMap getByCopyId(String copyId) {
        ReturnMap map = new ReturnMap();
        try {
            List<Log> logs = logRepository.findLogsByCopyIdOrderByTimeDesc(copyId);
            map.setRtn(1);
            map.setMessage("记录查询成功");
            map.put("logs", logs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("记录查询失败");
        }
        return map;
    }

    //许同樵 前端debug 添加
    public ReturnMap getAll() {
        ReturnMap map = new ReturnMap();
        try {
            List<Log> logs = logRepository.findAllByOrderByTimeDesc();
            map.setRtn(1);
            map.setMessage("记录查询成功");
            map.put("logs", logs);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("记录查询失败");
        }
        return map;
    }


}
