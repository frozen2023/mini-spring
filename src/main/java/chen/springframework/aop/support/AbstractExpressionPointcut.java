package chen.springframework.aop.support;

public abstract class AbstractExpressionPointcut implements ExpressionPointcut {

    private String expression;

    @Override
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

}
