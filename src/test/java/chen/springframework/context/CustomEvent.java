package chen.springframework.context;

import chen.springframework.context.event.ApplicationContextEvent;

import java.util.Date;

public class CustomEvent extends ApplicationContextEvent {

    int id;
    String info;

    public CustomEvent(ApplicationContext source, int id, String info) {
        super(source);
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
