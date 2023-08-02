package chen.springframework.aop.framework;

public interface AopProxyFactory {

    AopProxy createAopProxy(AdvisedSupport config);

}
