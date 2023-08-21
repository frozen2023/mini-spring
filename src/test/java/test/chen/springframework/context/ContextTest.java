package test.chen.springframework.context;

import test.chen.springframework.beans.UserService;
import chen.springframework.beans.factory.support.DefaultListableBeanFactory;
import chen.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import chen.springframework.context.support.ClassPathXmlApplicationContext;
import chen.springframework.core.type.AnnotationMetadata;
import chen.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import chen.springframework.stereotype.Component;
import org.junit.Test;

import java.util.Map;

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
    }

    @Test
    public void test_event() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:listener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1234, "成功了！"));
        applicationContext.registerShutdownHook();
    }

    @Test
    public void test_annotation() throws ClassNotFoundException {
        String name = AutowiredUser.class.getName();
        AnnotationMetadata annotationMetadata = new SimpleMetadataReaderFactory().getMetadataReader(name).getAnnotationMetadata();
        /*Annotation[] annotations = annotationMetadata.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation.annotationType());
        }*/

        /*Component annotation = annotationMetadata.getAnnotation(Component.class);
        System.out.println(annotation.value());

        Configuration annotation1 = annotationMetadata.getAnnotation(Configuration.class);
        System.out.println(annotation1 == null);*/

        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(Component.class.getName());
        for (String key : annotationAttributes.keySet()) {
            System.out.println("key:" + key + "   value:" + annotationAttributes.get(key));
        }


    }

}
