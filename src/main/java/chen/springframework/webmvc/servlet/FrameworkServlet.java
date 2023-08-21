package chen.springframework.webmvc.servlet;

import chen.springframework.context.ApplicationContext;
import chen.springframework.util.BeanUtils;
import chen.springframework.web.context.ConfigurableWebApplicationContext;
import chen.springframework.web.context.WebApplicationContext;
import chen.springframework.web.context.support.XmlWebApplicationContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public abstract class FrameworkServlet extends HttpServletBean {

    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;

    private WebApplicationContext webApplicationContext;

    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;

    private String contextConfigLocation;

    public Class<?> getContextClass() {
        return this.contextClass;
    }

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public String getContextConfigLocation() {
        return this.contextConfigLocation;
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            doService(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initServletBean() throws ServletException {
        // 加载  webApplicationContext
        this.webApplicationContext = initWebApplicationContext();
        initFrameworkServlet();

    }

    protected void initFrameworkServlet() throws ServletException {
    }

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext wac = null;
        if (this.webApplicationContext != null) {
            wac = this.webApplicationContext;
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
                configureAndRefreshWebApplicationContext(cwac);
            }
        }
        if (wac == null) {
            wac = createWebApplicationContext();
        }

        onRefresh(wac);

        return wac;
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
        wac.refresh();
    }

    protected WebApplicationContext createWebApplicationContext() {
        Class<?> clazz = getContextClass();
        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(clazz);

        String configLocation = getContextConfigLocation();
        if (configLocation != null) {
            wac.setConfigLocation(configLocation);
        }
        configureAndRefreshWebApplicationContext(wac);

        return wac;

    }

    protected void onRefresh(ApplicationContext context) {

    }


    protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
            throws Exception;
}
