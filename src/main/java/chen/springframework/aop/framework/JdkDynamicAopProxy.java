package chen.springframework.aop.framework;

import chen.springframework.aop.TargetSource;
import cn.hutool.core.lang.Assert;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements InvocationHandler, AopProxy {

    private final AdvisedSupport advised;

    private final Class<?>[] proxiedInterfaces;

    public JdkDynamicAopProxy(AdvisedSupport config) {
        Assert.notNull(config, "AdvisedSupport must not be null");
        this.advised = config;
        this.proxiedInterfaces = config.getProxiedInterfaces();
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), this.proxiedInterfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TargetSource targetSource = this.advised.getTargetSource();
        Object target = targetSource.getTarget();
        Class<?> targetClass = target.getClass();
        Object retVal;

        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

        if (chain.isEmpty()) {
            retVal = method.invoke(target, args);
        } else {
            MethodInvocation invocation =
                    new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            retVal = invocation.proceed();
        }

        return retVal;
    }
}
