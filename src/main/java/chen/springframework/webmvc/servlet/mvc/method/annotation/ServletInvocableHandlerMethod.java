package chen.springframework.webmvc.servlet.mvc.method.annotation;

import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.web.context.request.ServletWebRequest;
import chen.springframework.web.method.HandlerMethod;
import chen.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import chen.springframework.web.method.support.InvocableHandlerMethod;
import chen.springframework.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {

    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public ServletInvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public ServletInvocableHandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
        super(beanName, beanFactory, method);
    }

    public void setReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }

    public void invokeAndHandle(ServletWebRequest request, ModelAndView mav,
                                Object... providedArgs) throws Exception {
        Object retValue = invokeForRequest(request, mav, providedArgs);

        if (retValue == null) {
            mav.setRequestHandled(true);
            return;
        }

        mav.setRequestHandled(false);
        this.returnValueHandlers.handleReturnValue(retValue, getReturnValueType(retValue), mav, request);
    }
}
