package chen.springframework.core.type.classreading;

import java.io.IOException;

public interface MetadataReaderFactory {

    MetadataReader getMetadataReader(String className);

}
