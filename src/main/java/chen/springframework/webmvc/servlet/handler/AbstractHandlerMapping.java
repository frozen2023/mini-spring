package chen.springframework.webmvc.servlet.handler;

import chen.springframework.beans.BeanNameAware;
import chen.springframework.web.context.support.WebApplicationObjectSupport;
import chen.springframework.webmvc.servlet.HandlerExecutionChain;
import chen.springframework.webmvc.servlet.HandlerInterceptor;
import chen.springframework.webmvc.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHandlerMapping extends WebApplicationObjectSupport
        implements HandlerMapping, BeanNameAware {

    private String beanName;

    private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        Object handler = getHandlerInternal(request);
        if (handler == null) {
            return null;
        }

        if (handler instanceof String) {
            String handleName = (String) handler;
            handler = obtainApplicationContext().getBean(handleName);
        }

        HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

        return executionChain;
    }

    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
                (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));

        for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
                chain.addInterceptor(interceptor);
        }
        return chain;
    }

    protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;
}
