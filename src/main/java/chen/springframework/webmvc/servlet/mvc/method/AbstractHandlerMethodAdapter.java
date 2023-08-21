package chen.springframework.webmvc.servlet.mvc.method;

import chen.springframework.web.method.HandlerMethod;
import chen.springframework.webmvc.servlet.HandlerAdapter;
import chen.springframework.webmvc.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractHandlerMethodAdapter implements HandlerAdapter {


    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerMethod && supportsInternal((HandlerMethod) handler));
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    protected abstract ModelAndView handleInternal(HttpServletRequest request,
                                                   HttpServletResponse response, HandlerMethod handlerMethod) throws Exception;

    protected abstract boolean supportsInternal(HandlerMethod handlerMethod);
}
