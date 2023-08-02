package chen.springframework.aop.framework;

import chen.springframework.aop.Advisor;
import chen.springframework.aop.TargetSource;
import chen.springframework.aop.target.SingletonTargetSource;
import cn.hutool.core.lang.Assert;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdvisedSupport extends ProxyConfig implements Advised {

    private TargetSource targetSource;

    private AdvisorChainFactory advisorChainFactory;

    private transient Map<Integer, List<Object>> methodCache;

    private List<Class<?>> interfaces = new ArrayList<>();

    private List<Advisor> advisors = new ArrayList<>();

    public AdvisedSupport() {
        this.advisorChainFactory = DefaultAdvisorChainFactory.INSTANCE;
        this.methodCache = new ConcurrentHashMap<>(32);
    }

    public AdvisedSupport(Class<?>... interfaces) {
        this();
        setInterfaces(interfaces);
    }

    public void setTarget(Object target) {
        setTargetSource(new SingletonTargetSource(target));
    }

    public void setInterfaces(Class<?>... interfaces) {
        Assert.notNull(interfaces, "Interfaces must not be null");
        this.interfaces.clear();
        for (Class<?> ifc : interfaces) {
            addInterface(ifc);
        }
    }

    public void addInterface(Class<?> inf) {
        Assert.notNull(inf, "Interface must not be null");
        if (!inf.isInterface()) {
            throw new IllegalArgumentException("[" + inf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(inf)) {
            this.interfaces.add(inf);
        }
    }
    
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class<?>[0]);
    }

    @Override
    public void setTargetSource(TargetSource targetSource) {
        Assert.notNull(targetSource, "TargetSource must not be null");
        this.targetSource = targetSource;
    }

    @Override
    public TargetSource getTargetSource() {
        return this.targetSource;
    }

    @Override
    public Class<?> getTargetClass() {
        return getTargetSource().getTargetClass();
    }

    @Override
    public final Advisor[] getAdvisors() {
        return this.advisors.toArray(new Advisor[0]);
    }

    @Override
    public void addAdvisor(Advisor advisor) {
        this.advisors.add(advisor);
    }

    @Override
    public void addAdvisors(Collection<Advisor> advisors) {
        this.advisors.addAll(advisors);
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        Integer cacheKey = method.hashCode();
        List<Object> cached = this.methodCache.get(cacheKey);
        if (cached == null) {
            cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
                    this, method, targetClass);
            this.methodCache.put(cacheKey, cached);
        }
        return cached;
    }
}
