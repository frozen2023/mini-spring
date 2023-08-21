package chen.springframework.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public interface AnnotationMetadata {

    Annotation[] getAnnotations();

    String getClassName();

    <T> T getAnnotation(Class<T> requiredType);

    default Map<String, Object> getAnnotationAttributes(String annotationName) {
        Annotation[] annotations = getAnnotations();
        Annotation requiredAnnotation = null;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(annotationName)) {
                requiredAnnotation = annotation;
            }
        }

        if (requiredAnnotation == null) {
            return null;
        }

        Method[] methods = requiredAnnotation.annotationType().getDeclaredMethods();
        Map<String, Object> attributes = new HashMap<>();

        for (Method method : methods) {
            try {
                Object val = method.invoke(requiredAnnotation);
                String name = method.getName();
                attributes.put(name, val);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return attributes;
    }
}
