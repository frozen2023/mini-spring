package chen.springframework.web.method.support;


import chen.springframework.web.context.request.ServletWebRequest;
import chen.springframework.web.method.MethodParameter;
import chen.springframework.webmvc.servlet.ModelAndView;

public interface HandlerMethodReturnValueHandler {

    boolean supportsReturnType(MethodParameter returnType);

    void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndView mav, ServletWebRequest webRequest) throws Exception;

}
