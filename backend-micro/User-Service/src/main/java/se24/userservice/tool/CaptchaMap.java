package se24.userservice.tool;

import java.util.HashMap;
import java.util.Random;

public class CaptchaMap {
    private static final HashMap<String, Integer> captchaMap = new HashMap<>();
    private static final Random random = new Random();

    public static int generateCaptcha(String username) {
        int captcha = random.nextInt(9000);
        captcha += 1000;
        captchaMap.put(username, captcha);
        return captcha;
    }

    public static boolean isValid(String username, int captcha) {
        if (captchaMap.containsKey(username)) {
            return captchaMap.get(username) == captcha;
        } else {
            return false;
        }
    }
}
