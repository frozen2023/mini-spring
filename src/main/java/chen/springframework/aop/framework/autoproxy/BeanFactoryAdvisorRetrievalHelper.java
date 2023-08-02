package chen.springframework.aop.framework.autoproxy;

import chen.springframework.aop.Advisor;
import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import cn.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeanFactoryAdvisorRetrievalHelper {

    private final ConfigurableListableBeanFactory beanFactory;

    public BeanFactoryAdvisorRetrievalHelper(ConfigurableListableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
        this.beanFactory = beanFactory;
    }

    public List<Advisor> findAdvisorBeans() {
        Collection<Advisor> values = beanFactory.getBeansOfType(Advisor.class).values();
        return new ArrayList<>(values);
    }


}
