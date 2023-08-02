package chen.springframework.aop.framework;

import cn.hutool.core.lang.Assert;
import com.sun.istack.internal.Nullable;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class CglibAopProxy implements AopProxy {

    protected final AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport config)  {
        Assert.notNull(config, "AdvisedSupport must not be null");
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.advised.getTargetClass());
        enhancer.setInterfaces(this.advised.getProxiedInterfaces());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = advised.getTargetSource().getTarget();
            Class<?> targetClass = target.getClass();
            Object retVal;
            List<Object> chain = advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);

            if (chain.isEmpty()) {
                retVal = method.invoke(target, args);
            } else {
                retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass,chain, methodProxy);
            }

            return retVal;
        }
    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        public CglibMethodInvocation(Object proxy, @Nullable Object target, Method method,
                                     Object[] arguments, @Nullable Class<?> targetClass,
                                     List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {

            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
        }

        @Override
        public Object proceed() throws Throwable {
            return super.proceed();
        }
    }


}
