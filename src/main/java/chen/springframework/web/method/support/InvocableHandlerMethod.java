package chen.springframework.web.method.support;

import chen.springframework.beans.factory.BeanFactory;
import chen.springframework.web.context.request.ServletWebRequest;
import chen.springframework.web.method.HandlerMethod;
import chen.springframework.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class InvocableHandlerMethod extends HandlerMethod {

    public InvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public InvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public InvocableHandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
        super(beanName, beanFactory, method);
    }

    public Object invokeForRequest(ServletWebRequest request, ModelAndView mav,
                                   Object... providedArgs) throws Exception {
        Object[] args = getMethodArgumentValues(request, mav, providedArgs);
        return doInvoke(args);
    }

    protected Object[] getMethodArgumentValues(ServletWebRequest request, ModelAndView mav,
                                               Object... providedArgs) throws Exception {
        return null;
    }

    protected Object doInvoke(Object... args) throws Exception {

        Method method = getMethod();
        return method.invoke(getBean(), args);

    }


}
