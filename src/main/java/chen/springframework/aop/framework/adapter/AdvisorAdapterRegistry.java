package chen.springframework.aop.framework.adapter;

import chen.springframework.aop.Advisor;
import org.aopalliance.intercept.MethodInterceptor;

public interface AdvisorAdapterRegistry {

    MethodInterceptor[] getInterceptors(Advisor advisor);

    void registerAdvisorAdapter(AdvisorAdapter adapter);

}
