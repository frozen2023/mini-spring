package chen.springframework.web.context.support;

import chen.springframework.beans.factory.support.DefaultListableBeanFactory;
import chen.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {

    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

        beanDefinitionReader.setResourceLoader(this);

        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) {
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                reader.loadBeanDefinitions(configLocation);
            }
        }
    }

    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
    }

    @Override
    protected String[] getDefaultConfigLocations() {
        return new String[] {DEFAULT_CONFIG_LOCATION};
    }
}
