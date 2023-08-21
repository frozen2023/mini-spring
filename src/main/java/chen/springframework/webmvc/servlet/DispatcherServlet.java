package chen.springframework.webmvc.servlet;


import chen.springframework.context.ApplicationContext;
import com.sun.istack.internal.Nullable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DispatcherServlet extends FrameworkServlet {

    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

    private List<HandlerMapping> handlerMappings;

    private List<HandlerAdapter> handlerAdapters;

    private static Properties defaultStrategies;

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doDispatch(request, response);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecutionChain mappedHandler = null;
        ModelAndView mv = null;

        mappedHandler = getHandler(request);

        if (mappedHandler == null) {
            return;
        }

        HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

        if (!mappedHandler.applyPreHandle(request, response)) {
            return;
        }

        mv = ha.handle(request, response, mappedHandler.getHandler());

        mappedHandler.applyPostHandle(request, response, mv);

        processDispatchResult(request, response, mappedHandler, mv);

    }

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }
        throw new ServletException("No adapter for handler [" + handler +
                "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                       HandlerExecutionChain mappedHandler, ModelAndView mv) {
        if (mv == null) {
            return;
        }
    }

    @Override
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    protected void initStrategies(ApplicationContext context) {
        // 初始化mvc组件
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    private void initHandlerMappings(ApplicationContext context) {
        this.handlerMappings = null;
        Map<String, HandlerMapping> mappings = context.getBeansOfType(HandlerMapping.class);
        if (!mappings.isEmpty()) {
            this.handlerMappings = new ArrayList<>(mappings.values());
        }

        // 使用默认策略
        if (this.handlerMappings == null) {
            this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
        }
    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapters = null;
        Map<String, HandlerAdapter> adapters = context.getBeansOfType(HandlerAdapter.class);
        if (!adapters.isEmpty()) {
            this.handlerAdapters = new ArrayList<>(adapters.values());
        }

        if (this.handlerAdapters == null) {
            this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
        }
    }

    private void initMultipartResolver(ApplicationContext context) {}

    private void initLocaleResolver(ApplicationContext context) {}

    private void initThemeResolver(ApplicationContext context) {}

    private void initHandlerExceptionResolvers(ApplicationContext context) {}

    private void initRequestToViewNameTranslator(ApplicationContext context) {}

    private void initViewResolvers(ApplicationContext context) {}

    private void initFlashMapManager(ApplicationContext context) {}

    @SuppressWarnings("unchecked")
    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
        if (defaultStrategies == null) {
            try {
                Properties properties = new Properties();
                InputStream is = DispatcherServlet.class.getResourceAsStream(DEFAULT_STRATEGIES_PATH);
                properties.load(is);
                defaultStrategies = properties;
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " + ex.getMessage());
            }
        }

        String key = strategyInterface.getName();
        String value = defaultStrategies.getProperty(key);

        if (value != null) {
            String[] classNames = value.split(",");
            List<T> strategies = new ArrayList<>(classNames.length);
            for (String className : classNames) {
                try {
                    Class<?> beanClass = Class.forName(className);
                    Object defaultStrategy = createDefaultStrategy(context, beanClass);
                    strategies.add((T) defaultStrategy);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Could not find DispatcherServlet's default strategy class [" + className +
                            "] for interface [" + key + "]", e);
                }
            }
            return strategies;
        } else {
            return Collections.emptyList();
        }
    }

    protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
        return context.getAutowireCapableBeanFactory().createBean(clazz);
    }

}
