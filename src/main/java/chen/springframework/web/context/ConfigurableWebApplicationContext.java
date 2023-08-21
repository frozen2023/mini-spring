package chen.springframework.web.context;

import chen.springframework.context.ConfigurableApplicationContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext {

    void setServletContext(ServletContext servletContext);

    void setServletConfig(ServletConfig servletConfig);

    ServletConfig getServletConfig();

    void setConfigLocation(String configLocation);

    void setConfigLocations(String... configLocations);

    String[] getConfigLocations();
}
