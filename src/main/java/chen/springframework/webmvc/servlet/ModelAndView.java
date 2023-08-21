package chen.springframework.webmvc.servlet;

public class ModelAndView {

    private Object view;

    private boolean requestHandled = false;

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }

    public boolean isRequestHandled() {
        return this.requestHandled;
    }

}
