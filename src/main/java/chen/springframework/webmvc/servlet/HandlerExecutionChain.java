package chen.springframework.webmvc.servlet;

import com.sun.istack.internal.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HandlerExecutionChain {


    private final Object handler;

    private final List<HandlerInterceptor> interceptorList = new ArrayList<>();

    public HandlerExecutionChain(Object handler) {
        this(handler, (HandlerInterceptor[]) null);
    }

    public HandlerExecutionChain(Object handler, HandlerInterceptor... interceptors) {
        this(handler, (interceptors != null ? Arrays.asList(interceptors) : Collections.emptyList()));
    }

    public HandlerExecutionChain(Object handler, List<HandlerInterceptor> interceptorList) {
        this.handler = handler;
        this.interceptorList.addAll(interceptorList);
    }

    public Object getHandler() {
        return this.handler;
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        this.interceptorList.add(interceptor);
    }

    boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerInterceptor handlerInterceptor : this.interceptorList) {
            if (!handlerInterceptor.preHandle(request, response, getHandler())) {
                return false;
            }
        }
        return true;
    }

    void applyPostHandle(HttpServletRequest request, HttpServletResponse response, @Nullable ModelAndView mv)
            throws Exception {
        for (int i = this.interceptorList.size() - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = this.interceptorList.get(i);
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }


}
