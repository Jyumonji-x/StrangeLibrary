package fudan.se.lab2.store;

import fudan.se.lab2.domain.User;
import fudan.se.lab2.enums.UserPermissionEnum;

import java.util.HashMap;
import java.util.Map;

public class MyToken {
    protected static HashMap<Integer, User> tokenMap = new HashMap<>();

    public static void init() {
        tokenMap = new HashMap<>();
    }

    public String toString() {
        StringBuilder result = new StringBuilder("MyToken.toString called:\n");
        for (Map.Entry<Integer, User> entry : tokenMap.entrySet()
        ) {
            result.append("token:").append(entry.getKey()).append("\nuser:").append(entry.getValue().toString());
        }
        return result.toString();
    }

    public static User getUserByTokenId(Integer token) {
        return tokenMap.getOrDefault(token, null);
    }

    public static Integer insertToken(User user) {
        Integer tokenId = user.getUsername().hashCode();
        if (!tokenMap.containsKey(tokenId)) {
            tokenMap.put(tokenId, user);
        }
        return tokenId;
    }

    public static boolean isExist(Integer token) {
        User user = tokenMap.getOrDefault(token, null);
        return user != null;
    }

    public static boolean isAdmin(Integer token) {
        User user = tokenMap.getOrDefault(token, null);
        if (user == null) {
            return false;
        } else {
            return user.getPermission() > UserPermissionEnum.normalUser.getPermission();
        }
    }
}
