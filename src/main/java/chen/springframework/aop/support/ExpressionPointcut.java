package chen.springframework.aop.support;

import chen.springframework.aop.Pointcut;

public interface ExpressionPointcut extends Pointcut {

    String getExpression();

}
