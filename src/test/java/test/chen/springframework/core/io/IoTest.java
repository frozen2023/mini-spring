package test.chen.springframework.core.io;

import chen.springframework.stereotype.Controller;
import chen.springframework.util.AnnotationUtils;
import test.chen.springframework.beans.UserService;
import chen.springframework.beans.factory.support.DefaultListableBeanFactory;
import chen.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import chen.springframework.core.io.DefaultResourceLoader;
import chen.springframework.core.io.Resource;
import cn.hutool.core.io.IoUtil;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IoTest {
    private DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_file() throws IOException {
        Resource resource = resourceLoader.getResource("src/main/resources/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_xml() {
        // 1.初始化 BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2. 读取配置文件&注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");

        // 3. 获取Bean对象调用方法
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.userInfo();
    }

    @Test
    public void test_properties() {

        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = IoTest.class.getResourceAsStream("hello.properties");
            properties.load(resourceAsStream);
            Object hello = properties.get("hello");
            System.out.println(hello);
        } catch (IOException e) {
            System.out.println("error");
        }


    }

    @Test
    public void test_field() throws ServletException {
        System.out.println(AnnotationUtils.hasAnnotation(User.class, Controller.class));

    }

}
