package chen.springframework.context;

import chen.springframework.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();

}
