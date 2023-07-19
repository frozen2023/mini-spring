package chen.springframework.beans.factory.xml;

public class XmlReaderContext {

    private final XmlBeanDefinitionReader reader;

    public XmlReaderContext(XmlBeanDefinitionReader reader) {
        this.reader = reader;
    }

    public XmlBeanDefinitionReader getReader() {
        return reader;
    }
}
