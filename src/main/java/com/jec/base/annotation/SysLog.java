package com.jec.base.annotation;

import java.lang.annotation.*;

/**
 * Created by jeremyliu on 7/24/16.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String description()  default "";

    String action() default "";

    int[] params() default {};
}
