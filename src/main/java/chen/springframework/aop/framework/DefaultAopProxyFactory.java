package chen.springframework.aop.framework;

import java.lang.reflect.Proxy;

public class DefaultAopProxyFactory implements AopProxyFactory {

    public static final DefaultAopProxyFactory INSTANCE = new DefaultAopProxyFactory();

    // 实现接口使用jdk代理, 否则使用cglib代理
    @Override
    public AopProxy createAopProxy(AdvisedSupport config) {
        if (config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
            Class<?> targetClass = config.getTargetClass();
            if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                return new JdkDynamicAopProxy(config);
            }
            return new CglibAopProxy(config);
        }
        else {
            return new JdkDynamicAopProxy(config);
        }
    }

    private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
        int length = config.getProxiedInterfaces().length;
        return length == 0;
    }

}
