package chen.springframework.core.type.classreading;

import chen.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;

public class SimpleMetadataReader implements MetadataReader {

    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(String className) {
        this.annotationMetadata = doGetAnnotationMetadata(className);
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

    protected AnnotationMetadata doGetAnnotationMetadata(String className) {
        Class<?> targetClass;
        try {
            targetClass = Class.forName(className);
            Annotation[] annotations = targetClass.getAnnotations();
            return new SimpleAnnotationMetadata(className, annotations);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
