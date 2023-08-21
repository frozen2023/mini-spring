package chen.springframework.web.context.support;

import chen.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import chen.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import chen.springframework.web.context.ConfigurableWebApplicationContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableConfigApplicationContext
        implements ConfigurableWebApplicationContext {

    private ServletContext servletContext;

    private ServletConfig servletConfig;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        if (servletConfig != null && this.servletContext == null) {
            this.servletContext = servletConfig.getServletContext();
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public String[] getConfigLocations() {
        return super.getConfigLocations();
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
    }
}
