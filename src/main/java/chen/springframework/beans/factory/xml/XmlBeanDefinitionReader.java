package chen.springframework.beans.factory.xml;

import chen.springframework.beans.BeansException;
import chen.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import chen.springframework.beans.factory.support.BeanDefinitionReader;
import chen.springframework.beans.factory.support.BeanDefinitionRegistry;
import chen.springframework.core.io.InputStreamSource;
import chen.springframework.core.io.Resource;
import chen.springframework.core.io.ResourceLoader;
import cn.hutool.core.util.XmlUtil;
import com.sun.istack.internal.Nullable;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private DocumentLoader documentLoader = new DefaultDocumentLoader();

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void setDocumentLoader(@Nullable DocumentLoader documentLoader) {
        this.documentLoader = documentLoader != null ? documentLoader : new DefaultDocumentLoader();
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            InputStream inputStream = resource.getInputStream();
            return doLoadBeanDefinition(inputStream);
        } catch (IOException | ClassNotFoundException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public int loadBeanDefinitions(Resource... resources) throws BeansException {
        int cnt = 0;
        for (Resource resource : resources) {
            cnt += loadBeanDefinitions(resource);
        }
        return cnt;
    }

    @Override
    public int loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        return loadBeanDefinitions(resource);
    }

    public int doLoadBeanDefinition(InputStream inputStream) throws ClassNotFoundException {
            Document doc = doLoadDocument(inputStream);
            return registerBeanDefinitions(doc);
    }

    protected Document doLoadDocument(InputStream inputStream) {
        return this.documentLoader.loadDocument(inputStream);
    }

    public int registerBeanDefinitions(Document document) throws ClassNotFoundException {
        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        documentReader.registerBeanDefinitions(document, createReaderContext());
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }

    protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
        return new DefaultBeanDefinitionDocumentReader();
    }

    public XmlReaderContext createReaderContext() {
        return new XmlReaderContext(this);
    }


}
