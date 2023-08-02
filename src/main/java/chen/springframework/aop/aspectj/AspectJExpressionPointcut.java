package chen.springframework.aop.aspectj;

import chen.springframework.aop.ClassFilter;
import chen.springframework.aop.MethodMatcher;
import chen.springframework.aop.support.AbstractExpressionPointcut;
import chen.springframework.util.ClassUtils;
import com.sun.istack.internal.Nullable;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AspectJExpressionPointcut extends AbstractExpressionPointcut
        implements ClassFilter, MethodMatcher {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    private transient PointcutExpression pointcutExpression;

    private transient ClassLoader pointcutClassLoader;

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    public AspectJExpressionPointcut() {
    }

    public AspectJExpressionPointcut(String expression) {
        setExpression(expression);
    }

    public PointcutExpression getPointcutExpression() {
        return obtainPointcutExpression();
    }

    private PointcutExpression obtainPointcutExpression() {
        if (getExpression() == null) {
            throw new IllegalStateException("Must set property 'expression' before attempting to match");
        }
        if (this.pointcutExpression == null) {
            this.pointcutClassLoader = determinePointcutClassLoader();
            this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
        }
        return this.pointcutExpression;
    }

    private ClassLoader determinePointcutClassLoader() {
        return ClassUtils.getDefaultClassLoader();
    }

    private PointcutExpression buildPointcutExpression(@Nullable ClassLoader classLoader) {
        PointcutParser parser = initializePointcutParser(classLoader);
        return parser.parsePointcutExpression(getExpression());
    }

    private PointcutParser initializePointcutParser(@Nullable ClassLoader classLoader) {
        return PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, classLoader);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        PointcutExpression pointcutExpression = obtainPointcutExpression();
        return  pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        obtainPointcutExpression();
        ShadowMatch shadowMatch = getTargetShadowMatch(method, targetClass);
        return shadowMatch.alwaysMatches();
    }

    private ShadowMatch getTargetShadowMatch(Method method, Class<?> targetClass) {
        return obtainPointcutExpression().matchesMethodExecution(method);
    }

    @Override
    public ClassFilter getClassFilter() {
        obtainPointcutExpression();
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        obtainPointcutExpression();
        return this;
    }



}
