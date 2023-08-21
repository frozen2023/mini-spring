package chen.springframework.webmvc.servlet.mvc.method.annotation;

import chen.springframework.beans.factory.InitializingBean;
import chen.springframework.web.context.request.ServletWebRequest;
import chen.springframework.web.method.HandlerMethod;
import chen.springframework.web.method.support.HandlerMethodReturnValueHandler;
import chen.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import chen.springframework.webmvc.servlet.ModelAndView;
import chen.springframework.webmvc.servlet.mvc.method.AbstractHandlerMethodAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter
        implements InitializingBean {

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    @Override
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        ModelAndView mav;
        mav = invokeHandlerMethod(request, response, handlerMethod);
        return mav;
    }

    @Override
    protected boolean supportsInternal(HandlerMethod handlerMethod) {
        return true;
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
                                               HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
        if (this.returnValueHandlers != null) {
            invocableMethod.setReturnValueHandlers(this.returnValueHandlers);
        }
        ModelAndView mav = new ModelAndView();
        invocableMethod.invokeAndHandle(webRequest, mav);
        if (mav.isRequestHandled()) {
            return null;
        }
        return mav;
    }

    protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new ServletInvocableHandlerMethod(handlerMethod);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
        }
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        handlers.add(new RequestResponseBodyMethodProcessor());

        return handlers;
    }
}
