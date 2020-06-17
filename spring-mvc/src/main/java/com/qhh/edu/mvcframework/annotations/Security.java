package com.qhh.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {
    String value() default "";

    String pattern() default "";
}
