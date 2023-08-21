package chen.springframework.web.context;

import chen.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends ApplicationContext {

    ServletContext getServletContext();

}
