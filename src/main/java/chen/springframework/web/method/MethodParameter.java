package chen.springframework.web.method;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface MethodParameter {

    public <T extends Annotation> boolean hasMethodAnnotation(Class<T> annotationType);

    public Class<?> getContainingClass();

    public Method getMethod();

}
