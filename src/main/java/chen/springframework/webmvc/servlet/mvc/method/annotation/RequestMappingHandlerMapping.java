package chen.springframework.webmvc.servlet.mvc.method.annotation;

import chen.springframework.stereotype.Controller;
import chen.springframework.util.AnnotationUtils;
import chen.springframework.web.bind.annotation.RequestMapping;
import chen.springframework.webmvc.servlet.handler.AbstractHandlerMethodMapping;

public class RequestMappingHandlerMapping extends AbstractHandlerMethodMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotationUtils.hasAnnotation(beanType, Controller.class) || AnnotationUtils.hasAnnotation(beanType, RequestMapping.class);
    }

}
