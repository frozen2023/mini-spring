package test.chen.springframework.beans;

import chen.springframework.beans.BeanNameAware;
import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.*;
import chen.springframework.context.ApplicationContext;
import chen.springframework.context.ApplicationContextAware;

public class UserService implements InitializingBean, DisposableBean, BeanFactoryAware, BeanNameAware,
        ApplicationContextAware, BeanClassLoaderAware {

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

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

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void userInfo() {
        System.out.println("输出用户名：" + userDao.getUserName(id) + "  " + name + "  id: " +id);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行：UserService:destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行：UserService:afterPropertiesSet");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("beanName:" + name);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("beanClassLoader:" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
