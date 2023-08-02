package chen.springframework.aop.framework;


import chen.springframework.util.ClassUtils;

public class ProxyFactory extends ProxyCreatorSupport {

    public ProxyFactory() {
    }

    public ProxyFactory(Object target) {
        setTarget(target);
        setInterfaces(target.getClass().getInterfaces());
    }

    public ProxyFactory(Class<?>... proxyInterfaces) {
        setInterfaces(proxyInterfaces);
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }


}
