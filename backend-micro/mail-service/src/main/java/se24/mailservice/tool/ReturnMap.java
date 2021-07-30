package se24.mailservice.tool;

import java.util.HashMap;

public class ReturnMap {
    private HashMap<String, Object> map = new HashMap<>();

    public void setRtn(int rtn) {
        map.put("rtn", rtn);
    }

    public void setMessage(String message) {
        map.put("message", message);
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public void put(String key, Object obj) {
        map.put(key, obj);
    }
}
