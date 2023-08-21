package chen.springframework.web.method.support;

import chen.springframework.web.context.request.ServletWebRequest;
import chen.springframework.web.method.MethodParameter;
import chen.springframework.webmvc.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {

    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    public List<HandlerMethodReturnValueHandler> getHandlers() {
        return Collections.unmodifiableList(this.returnValueHandlers);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return getReturnValueHandler(returnType) != null;
    }

    private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndView mav, ServletWebRequest webRequest) throws Exception {
        HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown return value type: " + returnType.getContainingClass().getName());
        }
        handler.handleReturnValue(returnValue, returnType, mav, webRequest);
    }

    private HandlerMethodReturnValueHandler selectHandler(Object value, MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }

    public HandlerMethodReturnValueHandlerComposite addHandler(HandlerMethodReturnValueHandler handler) {
        this.returnValueHandlers.add(handler);
        return this;
    }

    public HandlerMethodReturnValueHandlerComposite addHandlers(List<? extends HandlerMethodReturnValueHandler> handlers) {
        if (handlers != null) {
            this.returnValueHandlers.addAll(handlers);
        }
        return this;
    }

}
