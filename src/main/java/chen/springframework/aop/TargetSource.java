package chen.springframework.aop;

public interface TargetSource {

    Class<?> getTargetClass();

    Object getTarget() throws Exception;
}
