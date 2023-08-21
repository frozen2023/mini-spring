package chen.springframework.context.annotation;

import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.core.type.AnnotationMetadata;
import chen.springframework.core.type.classreading.MetadataReader;
import chen.springframework.core.type.classreading.MetadataReaderFactory;

public class ConfigurationClassUtils {

    static boolean checkConfigurationClassCandidate(
            BeanDefinition beanDef, MetadataReaderFactory metadataReaderFactory) {
        String className = beanDef.getBeanClassName();
        if (className == null) {
            return false;
        }
        AnnotationMetadata metadata;

        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
        metadata = metadataReader.getAnnotationMetadata();

        Configuration conf = metadata.getAnnotation(Configuration.class);

        if (conf == null) {
            return false;
        }

        return true;
    }
}
