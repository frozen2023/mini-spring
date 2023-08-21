package chen.springframework.webmvc.servlet.mvc.method.annotation;

import chen.springframework.util.AnnotationUtils;
import chen.springframework.web.bind.annotation.ResponseBody;
import chen.springframework.web.context.request.ServletWebRequest;
import chen.springframework.web.method.MethodParameter;
import chen.springframework.web.method.support.HandlerMethodReturnValueHandler;
import chen.springframework.webmvc.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotationUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                    returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndView mav, ServletWebRequest webRequest) throws Exception {
        mav.setRequestHandled(true);
        HttpServletRequest request = webRequest.getRequest();
        HttpServletResponse response = webRequest.getResponse();
        writeWithMessageConverters(returnValue, returnType, request, response);
    }

    protected void writeWithMessageConverters(Object returnValue, MethodParameter returnType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        writer.write(returnValue.toString());
    }
}
