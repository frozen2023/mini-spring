package chen.springframework.aop.framework.autoproxy;

import chen.springframework.aop.Advisor;
import chen.springframework.aop.ClassFilter;
import chen.springframework.aop.PointcutAdvisor;
import chen.springframework.aop.TargetSource;
import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import cn.hutool.core.lang.Assert;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAdvisorAutoProxyCreator extends AbstractAutoProxyCreator {

    private BeanFactoryAdvisorRetrievalHelper advisorRetrievalHelper;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AdvisorAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        initBeanFactory((ConfigurableListableBeanFactory) beanFactory);
    }

    protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.advisorRetrievalHelper = new BeanFactoryAdvisorRetrievalHelper(beanFactory);
    }

    @Override
    protected List<Advisor> getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        List<Advisor> eligibleAdvisors = findEligibleAdvisors(beanClass, beanName);
        if (eligibleAdvisors.isEmpty()) {
            return null;
        }
        return eligibleAdvisors;
    }

    protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
        List<Advisor> candidateAdvisors = findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
        return eligibleAdvisors;
    }

    protected List<Advisor> findCandidateAdvisors() {
        Assert.state(this.advisorRetrievalHelper != null, "No BeanFactoryAdvisorRetrievalHelper available");
        return this.advisorRetrievalHelper.findAdvisorBeans();
    }

    protected List<Advisor> findAdvisorsThatCanApply (
            List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {
        if (candidateAdvisors == null) {
            return candidateAdvisors;
        }
        List<Advisor> eligibleAdvisors = new ArrayList<>();
        for (Advisor candidateAdvisor : candidateAdvisors) {
            if (canApply(candidateAdvisor, beanClass)) {
                eligibleAdvisors.add(candidateAdvisor);
            }
        }
        return eligibleAdvisors;
    }

    protected boolean canApply(Advisor advisor, Class<?> targetClass) {
        if (advisor instanceof PointcutAdvisor) {
            ClassFilter classFilter = ((PointcutAdvisor) advisor).getPointcut().getClassFilter();
            return classFilter.matches(targetClass);
        } else {
            return true;
        }
    }
}
