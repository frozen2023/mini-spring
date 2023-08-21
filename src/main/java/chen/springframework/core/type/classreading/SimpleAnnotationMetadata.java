package chen.springframework.core.type.classreading;

import chen.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.lang.ref.Reference;


public class SimpleAnnotationMetadata implements AnnotationMetadata {

    private final String className;

    private final Annotation[] annotations;

    public SimpleAnnotationMetadata(String className, Annotation[] annotations) {
        this.className = className;
        this.annotations = annotations;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public Annotation[] getAnnotations() {
        return this.annotations;
    }

    @Override
    public <T> T getAnnotation(Class<T> requiredType) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(requiredType.getName())) {
                return (T) annotation;
            }
        }
        return null;
    }


}
