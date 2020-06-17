package com.qhh.edu.mvcframework.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QhhRequestMapping {
String value() default "";
}
