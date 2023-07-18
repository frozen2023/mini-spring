package chen.springframework.beans;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("123", "chen");
        map.put("124", "zhan");
        map.put("125", "li");
    }

    public UserDao() {
    }

    public String getUserName(String key) {
        return map.get(key);
    }
}
