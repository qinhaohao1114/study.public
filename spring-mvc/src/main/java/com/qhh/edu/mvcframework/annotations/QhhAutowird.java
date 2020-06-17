package com.qhh.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QhhAutowird {
    String value() default "";
}
