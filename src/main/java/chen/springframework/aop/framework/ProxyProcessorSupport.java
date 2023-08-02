package chen.springframework.aop.framework;

public class ProxyProcessorSupport extends ProxyConfig {

    protected void evaluateProxyInterfaces(Class<?> beanClass, ProxyFactory proxyFactory) {

        Class<?>[] targetInterfaces = beanClass.getInterfaces();
        boolean hasReasonableProxyInterface = false;
        for (Class<?> ifc : targetInterfaces) {
            // 全是空接口使用cglib代理
            if (ifc.getMethods().length > 0) {
                hasReasonableProxyInterface = true;
                break;
            }
        }
        if (hasReasonableProxyInterface) {
            for (Class<?> ifc : targetInterfaces) {
                proxyFactory.addInterface(ifc);
            }
        } else {
            proxyFactory.setProxyTargetClass(true);
        }
    }
}
