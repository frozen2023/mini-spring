package chen.springframework.context.event;

import chen.springframework.context.ApplicationContext;

public class ContextRefreshedEvent extends ApplicationContextEvent {

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

}
