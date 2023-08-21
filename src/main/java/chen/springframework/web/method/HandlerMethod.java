package chen.springframework.web.method;

import chen.springframework.beans.factory.BeanFactory;
import com.sun.istack.internal.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object bean;

    private final BeanFactory beanFactory;

    private final Class<?> beanType;

    private final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
        this.beanFactory = null;
        this.beanType = bean.getClass();
    }

    public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
        this.bean = beanFactory.getBean(beanName);
        this.beanFactory = beanFactory;
        this.method = method;
        Class<?> beanType = beanFactory.getBean(beanName).getClass();
        if (beanType == null) {
            throw new IllegalStateException("Cannot resolve bean type for bean with name '" + beanName + "'");
        }
        this.beanType = beanType;
    }

    protected HandlerMethod(HandlerMethod handlerMethod) {
        this.bean = handlerMethod.bean;
        this.method = handlerMethod.method;
        this.beanFactory = handlerMethod.beanFactory;
        this.beanType = handlerMethod.beanType;
    }

    public Method getMethod() {
        return this.method;
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
        A a = getMethod().getAnnotation(annotationType);
        return a != null;
    }

    public HandlerMethodParameter getReturnValueType(Object returnValue) {
        return new ReturnValueMethodParameter(returnValue);
    }

    protected class HandlerMethodParameter implements MethodParameter {

        @Override
        public Method getMethod() {
            return HandlerMethod.this.getMethod();
        }

        @Override
        public <T extends Annotation> boolean hasMethodAnnotation(Class<T> annotationType) {
            return HandlerMethod.this.hasMethodAnnotation(annotationType);
        }

        @Override
        public Class<?> getContainingClass() {
            return HandlerMethod.this.getBeanType();
        }


    }

    private class ReturnValueMethodParameter extends HandlerMethodParameter {

        private final Class<?> returnValueType;

        public ReturnValueMethodParameter(Object returnValue) {
            this.returnValueType = (returnValue != null ? returnValue.getClass() : null);
        }

        public Class<?> getReturnValueType() {
            return returnValueType;
        }
    }
}
