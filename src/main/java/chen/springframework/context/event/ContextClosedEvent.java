package chen.springframework.context.event;

import chen.springframework.context.ApplicationContext;

public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }

}
