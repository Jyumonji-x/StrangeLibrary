package se24.loggerservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import se24.loggerservice.domain.Log;
import se24.loggerservice.repository.LogRepository;
import se24.loggerservice.service.LogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class LoggerServiceApplicationTests {

    @Mock
    private LogRepository logRepository;
    @InjectMocks
    private LogService logService;

    private final String mockUsername = "lbw";
    private final String mockCopyId = "1234567";

    /**
     * addLog
     **/

    @Test
    void addLog(){
        Log log = new Log();
        HashMap<String, Object> map = logService.add(log).getMap();
        assertEquals("创建记录成功", map.get("message"));
        System.out.println(logService.add(log).getMap());
    }

    /**
     * get Log
     **/

    @Test
    void getLogByUsername(){
        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setUsername(mockUsername);
        logs.add(log);
        Mockito.when(logRepository.findLogsByUsernameOrderByTimeDesc(mockUsername)).thenReturn(logs);
        HashMap<String, Object> map = logService.getByUsername(mockUsername).getMap();
        assertEquals("记录查询成功", map.get("message"));
        System.out.println(logService.getByUsername(mockUsername).getMap());
    }

    @Test
    void getLogByCopyId(){
        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setCopyId(mockCopyId);
        logs.add(log);

        Mockito.when(logRepository.findLogsByCopyIdOrderByTimeDesc(mockCopyId)).thenReturn(logs);

        HashMap<String, Object> map = logService.getByCopyId(mockCopyId).getMap();
        assertEquals("记录查询成功", map.get("message"));
        System.out.println(logService.getByCopyId(mockCopyId).getMap());
    }

    @Test
    void getAllLog(){
        List<Log> logs = new ArrayList<>();
        Log log = new Log();
        log.setCopyId(mockCopyId);
        logs.add(log);
        Mockito.when(logRepository.findAll()).thenReturn(logs);
        HashMap<String, Object> map = logService.getAll().getMap();
        assertEquals("记录查询成功", map.get("message"));
        System.out.println(logService.getAll().getMap());
    }



}
