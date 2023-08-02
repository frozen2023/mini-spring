package chen.springframework.aop.framework;

import chen.springframework.aop.Advisor;
import chen.springframework.aop.MethodMatcher;
import chen.springframework.aop.PointcutAdvisor;
import chen.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import chen.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultAdvisorChainFactory implements AdvisorChainFactory {

    public static final DefaultAdvisorChainFactory INSTANCE = new DefaultAdvisorChainFactory();

    @Override
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, Class<?> targetClass) {
        AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
        Advisor[] advisors = config.getAdvisors();
        List<Object> interceptorList = new ArrayList<>(advisors.length);
        Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());


        for (Advisor advisor : advisors) {
            if (advisor instanceof PointcutAdvisor) {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                if (pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                    MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                    boolean match;
                    match = mm.matches(method, actualClass);

                    if (match) {
                        MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                        interceptorList.addAll(Arrays.asList(interceptors));
                    }
                }
            }
        }
        return interceptorList;
    }
}
