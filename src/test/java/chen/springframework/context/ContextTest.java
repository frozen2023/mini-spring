package chen.springframework.context;

import chen.springframework.beans.UserService;
import chen.springframework.beans.factory.support.DefaultListableBeanFactory;
import chen.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import chen.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class ContextTest {

    @Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        // 5. 获取Bean对象调用方法
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.userInfo();
    }

    @Test
    public void test_xml() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();

        // 2. 获取Bean对象调用方法
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.userInfo();
        System.out.println("ApplicationContext:" + userService.getApplicationContext());
        System.out.println("BeanFactory:" + userService.getBeanFactory());
    }

}
