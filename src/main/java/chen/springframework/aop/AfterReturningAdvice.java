package chen.springframework.aop;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Method;

public interface AfterReturningAdvice extends AfterAdvice {

    void afterReturning(@Nullable Object returnValue, Method method, Object[] args, @Nullable Object target) throws Throwable;

}
