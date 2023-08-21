package chen.springframework.core.type.classreading;

import chen.springframework.core.io.DefaultResourceLoader;
import chen.springframework.core.io.ResourceLoader;

public class SimpleMetadataReaderFactory implements MetadataReaderFactory {

    private final ResourceLoader resourceLoader;

    public SimpleMetadataReaderFactory() {
        this.resourceLoader = new DefaultResourceLoader();
    }

    public SimpleMetadataReaderFactory(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    @Override
    public MetadataReader getMetadataReader(String className) {
        return new SimpleMetadataReader(className);
    }
}
