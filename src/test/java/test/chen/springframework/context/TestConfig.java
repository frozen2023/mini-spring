package test.chen.springframework.context;

import chen.springframework.context.annotation.ComponentScan;
import chen.springframework.context.annotation.Configuration;
import sun.misc.Contended;

@Configuration
@ComponentScan(basePackages = {"test.chen.springframework.context"})
public class TestConfig {

    public void print() {
        System.out.println("类：TestConfig" );
    }
}
