package chen.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class AnnotationAttributes extends HashMap<String, Object> {

    private final Class<? extends Annotation> annotationType;

    public AnnotationAttributes() {
        this.annotationType = null;
    }

    public AnnotationAttributes(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public AnnotationAttributes(Map<String, Object> map) {
        super(map);
        this.annotationType = null;
    }

    public static AnnotationAttributes fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        if (map instanceof AnnotationAttributes) {
            return (AnnotationAttributes) map;
        }

        return new AnnotationAttributes(map);
    }

    public String[] getStringArray(String attributeName) {
        return getRequiredAttribute(attributeName, String[].class);
    }

    private <T> T getRequiredAttribute(String attributeName, Class<T> expectedType) {
        Object value = get(attributeName);

        if (!expectedType.isInstance(value) && expectedType.isArray() &&
                expectedType.getComponentType().isInstance(value)) {
            Object array = Array.newInstance(expectedType.getComponentType(), 1);
            Array.set(array, 0, value);
            value = array;
        }

        return (T) value;
    }
}
