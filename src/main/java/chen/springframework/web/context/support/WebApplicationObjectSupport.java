package chen.springframework.web.context.support;

import chen.springframework.beans.BeansException;
import chen.springframework.context.ApplicationContext;
import chen.springframework.context.support.ApplicationObjectSupport;
import chen.springframework.web.context.ServletContextAware;
import chen.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class WebApplicationObjectSupport extends ApplicationObjectSupport implements ServletContextAware {

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        if (servletContext != this.servletContext) {
            this.servletContext = servletContext;
            initServletContext(this.servletContext);
        }
    }

    @Override
    protected void initApplicationContext(ApplicationContext context) throws BeansException {
        super.initApplicationContext(context);
        if (this.servletContext == null && context instanceof WebApplicationContext) {
            this.servletContext = ((WebApplicationContext) context).getServletContext();
            if (this.servletContext != null) {
                initServletContext(this.servletContext);
            }
        }
    }

    protected void initServletContext(ServletContext servletContext) {
    }

    protected final WebApplicationContext getWebApplicationContext() throws IllegalStateException {
        ApplicationContext ctx = getApplicationContext();
        if (ctx instanceof WebApplicationContext) {
            return (WebApplicationContext) getApplicationContext();
        } else {
            return null;
        }
    }

    protected final ServletContext getServletContext() throws IllegalStateException {
        if (this.servletContext != null) {
            return this.servletContext;
        }
        ServletContext servletContext = null;
        WebApplicationContext wac = getWebApplicationContext();
        if (wac != null) {
            servletContext = wac.getServletContext();
        }
        if (servletContext == null) {
            throw new IllegalStateException("WebApplicationObjectSupport instance [" + this +
                    "] does not run within a ServletContext. Make sure the object is fully configured!");
        }
        return servletContext;
    }
}
