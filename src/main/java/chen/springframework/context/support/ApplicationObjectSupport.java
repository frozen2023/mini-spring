package chen.springframework.context.support;

import chen.springframework.beans.BeansException;
import chen.springframework.context.ApplicationContext;
import chen.springframework.context.ApplicationContextAware;
import cn.hutool.core.lang.Assert;

public abstract class ApplicationObjectSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (context == null) {
            this.applicationContext = null;
        } else if (this.applicationContext == null) {
            if(!requiredContextClass().isInstance(context)) {
                throw new RuntimeException("Invalid application context: needs to be of type [" + this.requiredContextClass().getName() + "]");
            }
            this.applicationContext = context;
            initApplicationContext();
        }
    }

    public final ApplicationContext getApplicationContext() throws IllegalStateException {
        return this.applicationContext;
    }

    protected final ApplicationContext obtainApplicationContext() {
        ApplicationContext applicationContext = getApplicationContext();
        Assert.state(applicationContext != null, "No ApplicationContext");
        return applicationContext;
    }

    protected Class<?> requiredContextClass() {
        return ApplicationContext.class;
    }

    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        this.initApplicationContext();
    }

    protected void initApplicationContext() throws BeansException {
    }

}
