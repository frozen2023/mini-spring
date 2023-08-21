package chen.springframework.beans.factory.annotation;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.PropertyValues;
import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.beans.factory.BeanFactoryAware;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import chen.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.hutool.core.bean.BeanUtil;
import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();

        for (Field field : fields) {
            Autowired autowiredAnno = field.getAnnotation(Autowired.class);
            if (autowiredAnno != null) {
                Class<?> fieldType = field.getType();
                Object dependentBean = null;
                Qualifier qualifierAnno = field.getAnnotation(Qualifier.class);
                if (qualifierAnno != null) {
                    String requiredBeanName = qualifierAnno.value();
                    dependentBean = beanFactory.getBean(requiredBeanName, fieldType);
                } else {
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
            }
        }

        return pvs;

    }
}
