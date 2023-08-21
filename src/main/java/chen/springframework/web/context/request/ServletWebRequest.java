package chen.springframework.web.context.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletWebRequest {

    HttpServletRequest request;

    HttpServletResponse response;

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }
}
