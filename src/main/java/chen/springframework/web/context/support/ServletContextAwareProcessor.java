package chen.springframework.web.context.support;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.config.BeanPostProcessor;
import chen.springframework.web.context.ServletConfigAware;
import chen.springframework.web.context.ServletContextAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletContextAwareProcessor implements BeanPostProcessor {

    private ServletContext servletContext;

    private ServletConfig servletConfig;

    public ServletContextAwareProcessor() {
    }

    public ServletContextAwareProcessor(ServletContext servletContext) {
        this(servletContext, null);
    }

    public ServletContextAwareProcessor(ServletConfig servletConfig) {
        this(null, servletConfig);
    }

    public ServletContextAwareProcessor(ServletContext servletContext, ServletConfig servletConfig) {
        this.servletContext = servletContext;
        this.servletConfig = servletConfig;
    }

    public ServletContext getServletContext() {
        if (this.servletContext == null && getServletConfig() != null) {
            return getServletConfig().getServletContext();
        }
        return this.servletContext;
    }

    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (getServletContext() != null && bean instanceof ServletContextAware) {
            ((ServletContextAware) bean).setServletContext(getServletContext());
        }

        if (getServletConfig() != null && bean instanceof ServletConfigAware) {
            ((ServletConfigAware) bean).setServletConfig(getServletConfig());
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
