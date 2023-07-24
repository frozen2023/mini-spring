package chen.springframework.beans;

import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private static Map<String, String> map = new HashMap<>();

    public void initDataMethod() {
        map.put("123", "chen");
        map.put("124", "zhan");
        map.put("125", "li");
    }

    public void destroyDataMethod(){
        System.out.println("执行：destroy-method");
        map.clear();
    }

    public UserDao() {
    }

    public String getUserName(String key) {
        return map.get(key);
    }
}
