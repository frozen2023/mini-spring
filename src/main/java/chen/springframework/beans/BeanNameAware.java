package chen.springframework.beans;

import chen.springframework.beans.factory.Aware;

public interface BeanNameAware extends Aware {

    void setBeanName(String name);

}
