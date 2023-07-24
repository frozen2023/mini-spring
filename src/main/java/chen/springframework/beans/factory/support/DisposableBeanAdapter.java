package chen.springframework.beans.factory.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.DisposableBean;
import chen.springframework.beans.factory.config.BeanDefinition;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodName;
    private final boolean invokeDisposableBean;


    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
        this.invokeDisposableBean = bean instanceof DisposableBean;
    }

    @Override
    public void destroy() throws Exception {

        if (this.invokeDisposableBean) {
            ((DisposableBean) this.bean).destroy();
        }

        if (StrUtil.isNotEmpty(destroyMethodName) && !(this.invokeDisposableBean && "destroy".equals(this.destroyMethodName))) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            destroyMethod.invoke(bean);
        }
    }
}
