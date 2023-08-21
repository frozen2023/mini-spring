package chen.springframework.util;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class AnnotationUtils {

    private static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType, Set<Annotation> visited) {
        try {
            // 判断该类是否被该注解直接标记，对应第①种情况
            A annotation = clazz.getDeclaredAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            // 递归检索该类注解的注解中是否存在目标注解，对应第②种情况
            for (Annotation declaredAnn : clazz.getDeclaredAnnotations()) {
                Class<? extends Annotation> declaredType = declaredAnn.annotationType();
                if (!isInJavaLangAnnotationPackage(declaredType) && visited.add(declaredAnn)) {
                    // 进入递归
                    annotation = findAnnotation(declaredType, annotationType, visited);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            }
        }
        catch (Throwable ex) {
            return null;
        }
        // 递归检索该类的接口中是否存在目标注解，对应第③种情况
        for (Class<?> ifc : clazz.getInterfaces()) {
            A annotation = findAnnotation(ifc, annotationType, visited);
            if (annotation != null) {
                return annotation;
            }
        }
        // 递归检索该类的父类是否存在目标注解，对应第④种情况
        Class<?> superclass = clazz.getSuperclass();
        // 如果父类不存在或者父类为Object，则直接返回null
        if (superclass == null || Object.class == superclass) {
            return null;
        }
        return findAnnotation(superclass, annotationType, visited);
    }

    public static boolean isInJavaLangAnnotationPackage(Class<?> declaredType) {
        return declaredType.getName().startsWith("java.lang.annotation");
    }

    public static <A extends Annotation> boolean hasAnnotation(Class<?> clazz, Class<A> annotationType) {
        Set<Annotation> set = new HashSet<>();
        A annotation = findAnnotation(clazz, annotationType, set);
        if (annotation == null) {
            return false;
        }
        return true;
    }

}
