package se24.userservice.tool;

import org.springframework.util.DigestUtils;
import se24.userservice.domain.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SessionMap {
    protected static HashMap<String, User> map = new HashMap<>();

    public static String login(User user) {
        String session = DigestUtils.md5DigestAsHex(user.getUsername().getBytes(StandardCharsets.UTF_8));
        map.put(session, user);
        System.out.println("login() session = " + session);
        return session;
    }

    public static User getUser(String session) {
        return map.getOrDefault(session, null);
    }
}