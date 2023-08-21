package test.chen.springframework.beans;

import chen.springframework.beans.MutablePropertyValues;
import chen.springframework.beans.PropertyValue;
import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanReference;
import chen.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class BeansTest {

    @Test
    public void test01() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(TestService.class);
        factory.registerBeanDefinition("testService", beanDefinition);
        TestService testService1 = (TestService) factory.getBean("testService");
        testService1.test();
        System.out.println(testService1);
        TestService testService2 = (TestService) factory.getBean("testService");
        testService2.test();
        System.out.println(testService2);
    }

    @Test
    public void test02() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        factory.registerBeanDefinition("userService", beanDefinition);

        UserService bean = (UserService)factory.getBean("userService", "chen");
        bean.userInfo();
    }

    @Test
    public void test03() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "chen"));
        propertyValues.addPropertyValue(new PropertyValue("id", "124"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", beanDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.userInfo();
    }

    @Test
    public void test() {
        System.out.println(UserService.class.getName());
    }
}
