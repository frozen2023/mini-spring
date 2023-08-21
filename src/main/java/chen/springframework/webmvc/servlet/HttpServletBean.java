package chen.springframework.webmvc.servlet;

import cn.hutool.core.bean.BeanUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class HttpServletBean extends HttpServlet {

    @Override
    public void init() throws ServletException {
        setPropertyValues();
        initServletBean();
    }

    protected void initServletBean() throws ServletException {
    }

    protected void setPropertyValues() {
        ServletConfig servletConfig = getServletConfig();
        String contextConfigLocation = servletConfig.getInitParameter("contextConfigLocation");
        BeanUtil.setFieldValue(this, "contextConfigLocation", contextConfigLocation);
    }

}
