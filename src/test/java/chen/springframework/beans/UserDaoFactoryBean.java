package chen.springframework.beans;

import chen.springframework.beans.factory.FactoryBean;

public class UserDaoFactoryBean implements FactoryBean<UserDao> {
    @Override
    public UserDao getObject() throws Exception {
        System.out.println("....开始创建UserDao....");
        UserDao userDao = new UserDao();
        userDao.initDataMethod();
        return userDao;
    }

    @Override
    public Class<?> getObjectType() {
        return UserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
