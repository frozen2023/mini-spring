package chen.springframework.beans;

public class UserService {
    private String name;

    private String id;

    private UserDao userDao;

    public UserService() {
    }

    public UserService(String name) {
        this.name = name;
    }

    public UserService(String name, String id, UserDao userDao) {
        this.name = name;
        this.id = id;
        this.userDao = userDao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void userInfo() {
        System.out.println("输出用户名：" + userDao.getUserName(id) + "  " + name + "  id: " +id);
    }
}
