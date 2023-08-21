package test.chen.springframework.context;

import chen.springframework.context.annotation.ConfigurationClassPostProcessor;
import chen.springframework.context.support.ClassPathXmlApplicationContext;
import chen.springframework.stereotype.Component;
import cn.hutool.core.util.ClassUtil;
import org.junit.Test;


public class AutowiredTest {

    @Test
    public void testScanPackages() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:auto.xml");
     /*   TestConfig config = context.getBean("config", TestConfig.class);
        config.print();*/

        /*ConfigurationClassPostProcessor bean = context.getBean("configurationClassPostProcessor", ConfigurationClassPostProcessor.class);
        System.out.println(bean != null);*/
        Custom custom = context.getBean("custom", Custom.class);
        custom.info();

       /* Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation("test.chen.springframework.context", Component.class);
        System.out.println(classes.size());*/
    }
}
