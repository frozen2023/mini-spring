package chen.springframework.web.context;

import chen.springframework.beans.factory.Aware;

import javax.servlet.ServletConfig;

public interface ServletConfigAware extends Aware {

    void setServletConfig(ServletConfig servletConfig);

}
