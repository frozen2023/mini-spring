package chen.springframework.beans.factory.config;

import chen.springframework.beans.MutablePropertyValues;

import java.awt.*;

// bean 定义类
public class BeanDefinition {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    public static final String SCOPE_DEFAULT = "";

    private Class beanClass;

    private MutablePropertyValues propertyValues;

    private String initMethodName;

    private String destroyMethodName;

    private String scope = SCOPE_DEFAULT;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new MutablePropertyValues();
    }

    public BeanDefinition(Class beanClass, MutablePropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new MutablePropertyValues();
    }

    public BeanDefinition(Class beanClass, MutablePropertyValues propertyValues, String initMethodName, String destroyMethodName) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return getBeanClass().getName();
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope) || SCOPE_DEFAULT.equals(this.scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
