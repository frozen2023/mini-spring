package chen.springframework.context.annotation;

import chen.springframework.beans.factory.config.BeanDefinition;
import chen.springframework.beans.factory.config.BeanDefinitionHolder;
import chen.springframework.beans.factory.support.BeanDefinitionRegistry;
import chen.springframework.core.type.AnnotationMetadata;
import chen.springframework.core.type.classreading.MetadataReader;
import chen.springframework.core.type.classreading.MetadataReaderFactory;
import chen.springframework.stereotype.Component;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ConfigurationClassParser {

    private final MetadataReaderFactory metadataReaderFactory;

    private final BeanDefinitionRegistry registry;

    private final ComponentScanAnnotationParser componentScanAnnotationParser;

    public ConfigurationClassParser(MetadataReaderFactory metadataReaderFactory, BeanDefinitionRegistry registry) {
        this.metadataReaderFactory = metadataReaderFactory;
        this.registry = registry;
        this.componentScanAnnotationParser = new ComponentScanAnnotationParser(registry);
    }

    public void parse(Set<BeanDefinitionHolder> configCandidates) {

        for (BeanDefinitionHolder bdh : configCandidates) {
            BeanDefinition beanDefinition = bdh.getBeanDefinition();
            parse(beanDefinition.getBeanClassName(), bdh.getBeanName());
        }
    }

    protected final void parse(String className, String beanName) {
        MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(className);
        processConfigurationClass(metadataReader);

    }

    protected void processConfigurationClass(MetadataReader metadataReader) {
        SourceClass sourceClass = new SourceClass(metadataReader);
        doProcessConfigurationClass(metadataReader, sourceClass);
    }

    protected final SourceClass doProcessConfigurationClass(
            MetadataReader metadataReader, SourceClass sourceClass) {

        Set<AnnotationAttributes> componentScans = attributesForRepeatable(metadataReader.getAnnotationMetadata(), ComponentScan.class.getName());
        if (!componentScans.isEmpty()) {
            for (AnnotationAttributes componentScan : componentScans) {
                Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanAnnotationParser.parse(componentScan, sourceClass.getMetadata().getClassName());
                for (BeanDefinitionHolder bdh : scannedBeanDefinitions) {
                    BeanDefinition beanDefinition = bdh.getBeanDefinition();
                    if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDefinition, this.metadataReaderFactory)) {
                        parse(beanDefinition.getBeanClassName(), bdh.getBeanName());
                    }
                }
            }
        }

        return null;
    }

    protected Set<AnnotationAttributes> attributesForRepeatable(AnnotationMetadata annotationMetadata, String annotationClassName) {
        Set<AnnotationAttributes> result = new HashSet<>();
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(annotationClassName);
        result.add(AnnotationAttributes.fromMap(annotationAttributes));
        return result;
    }


    private class SourceClass {

        private final Object source;

        private final AnnotationMetadata metadata;

        public SourceClass(Object source) {
            this.source = source;
            this.metadata = ((MetadataReader) source).getAnnotationMetadata();
        }

        public Object getSource() {
            return source;
        }

        public AnnotationMetadata getMetadata() {
            return metadata;
        }


    }

}
