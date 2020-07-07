package com.qhh.springweb_demo.annotations;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SelfTask {


    String cron() default "";

    String taskName() default "";
}
