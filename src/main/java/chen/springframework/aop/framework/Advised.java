package chen.springframework.aop.framework;

import chen.springframework.aop.Advisor;
import chen.springframework.aop.TargetSource;

import java.util.Collection;

public interface Advised {

    void setTargetSource(TargetSource targetSource);

    TargetSource getTargetSource();

    public Class<?> getTargetClass();

    Advisor[] getAdvisors();

    void addAdvisor(Advisor advisor);

    void addAdvisors(Collection<Advisor> advisors);

}
