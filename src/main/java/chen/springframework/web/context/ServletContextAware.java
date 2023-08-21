package chen.springframework.web.context;

import chen.springframework.beans.factory.Aware;

import javax.servlet.ServletContext;

public interface ServletContextAware extends Aware {

    void setServletContext(ServletContext servletContext);

}
