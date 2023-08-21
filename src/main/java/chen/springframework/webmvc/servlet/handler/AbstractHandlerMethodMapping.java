package chen.springframework.webmvc.servlet.handler;

import chen.springframework.beans.factory.InitializingBean;
import chen.springframework.web.bind.annotation.RequestMapping;
import chen.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractHandlerMethodMapping extends AbstractHandlerMapping implements InitializingBean {

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        String lookupPath = initLookupPath(request);
        return lookupHandlerMethod(lookupPath, request);
    }

    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        List<String> beatMapping;
        List<List<String>> directPathMatches = this.mappingRegistry.getMappingsByDirectPath(lookupPath);
        if (directPathMatches != null) {
            beatMapping = directPathMatches.get(0);
            if (directPathMatches.size() > 1) {
                String uri = request.getRequestURI();
                throw new IllegalStateException(
                        "Ambiguous handler methods mapped for '" + uri);
            }

            Map<List<String>, MappingRegistration> registry = this.mappingRegistry.getRegistry();
            return registry.get(beatMapping).getHandlerMethod();
        } else {
            return null;
        }
    }

    protected String initLookupPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerMethods();
    }

    protected void initHandlerMethods() {
        for (String beanName : getCandidateBeanNames()) {
            processCandidateBean(beanName);
        }
    }

    protected String[] getCandidateBeanNames() {
        return obtainApplicationContext().getBeanNamesForType(Object.class);
    }

    protected void processCandidateBean(String beanName) {
        Class<?> beanType = null;
        beanType = obtainApplicationContext().getBean(beanName).getClass();

        if (beanType != null && isHandler(beanType)) {
            detectHandlerMethods(beanName);
        }
    }

    protected void detectHandlerMethods(Object handler) {
        Class<?> handlerType = (handler instanceof String ?
                obtainApplicationContext().getBean((String) handler).getClass() : handler.getClass());
        if (handlerType != null) {
            Map<Method, List<String>> methods = selectMethods(handlerType);
            methods.forEach((method, mapping) -> {
                registerHandlerMethod(handler, method, mapping);
            });
        }
    }

    protected Map<Method, List<String>> selectMethods(Class<?> beanType) {
        Map<Method, List<String>> result = new HashMap<>();
        Method[] methods = beanType.getDeclaredMethods();
        String[] baseUrls = null;
        RequestMapping classPathAnno = beanType.getAnnotation(RequestMapping.class);
        if (classPathAnno != null) {
            baseUrls = classPathAnno.value();
        }
        for (Method method : methods) {
            RequestMapping methodPathAnno = method.getAnnotation(RequestMapping.class);
            if (methodPathAnno == null) {
                continue;
            }
            String[] methodUrls = methodPathAnno.value();
            List<String> directPaths = new ArrayList<>();
            StringBuilder directPath = new StringBuilder();
            for (String methodUrl : methodUrls) {
                if (baseUrls != null) {
                    for (String baseUrl : baseUrls) {
                        directPath.append(baseUrl);
                        directPath.append(methodUrl);
                        directPaths.add(directPath.toString());
                    }
                } else {
                    directPaths.add(methodUrl);
                }
            }
            result.put(method, directPaths);
        }

        return result;
    }

    protected void registerHandlerMethod(Object handler, Method method, List<String> mapping) {
        this.mappingRegistry.register(mapping, handler, method);
    }

    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        if (handler instanceof String) {
            return new HandlerMethod((String) handler,
                    obtainApplicationContext().getAutowireCapableBeanFactory(),
                    method);
        }
        return new HandlerMethod(handler, method);
    }

    protected abstract boolean isHandler(Class<?> beanType);



    class MappingRegistry {

        private final HashMap<String, List<List<String>>> pathLookup = new HashMap<>();

        private final Map<List<String>, MappingRegistration> registry = new HashMap<>();

        public void register(List<String> mapping, Object handler, Method method) {
            HandlerMethod handlerMethod = createHandlerMethod(handler, method);

            for (String path : mapping) {
                List<List<String>> list = pathLookup.get(path);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(mapping);
                pathLookup.put(path, list);
            }

            this.registry.put(mapping, new MappingRegistration(mapping, handlerMethod));
        }
        
        public List<List<String>> getMappingsByDirectPath(String urlPath) {
            return this.pathLookup.get(urlPath);
        }

        public Map<List<String>, MappingRegistration> getRegistry() {
            return registry;
        }
    }

    static class MappingRegistration {

        private final List<String> mapping;

        private final HandlerMethod handlerMethod;

        public MappingRegistration(List<String> mapping, HandlerMethod handlerMethod) {
            this.mapping = mapping;
            this.handlerMethod = handlerMethod;
        }

        public List<String> getMapping() {
            return this.mapping;
        }

        public HandlerMethod getHandlerMethod() {
            return this.handlerMethod;
        }
    }
}
