package chen.springframework.beans.factory;

import chen.springframework.beans.BeansException;

@FunctionalInterface
public interface ObjectFactory<T> {

	T getObject() throws BeansException;

}
