package chen.springframework.context;

@FunctionalInterface
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
