package com.qhh.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QhhController {
    String value() default "";
}
