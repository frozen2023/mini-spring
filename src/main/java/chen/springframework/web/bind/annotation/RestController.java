package chen.springframework.web.bind.annotation;

import chen.springframework.stereotype.Controller;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
	String value() default "";
}
