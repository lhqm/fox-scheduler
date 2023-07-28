package com.foxrpc.annotation;


import com.foxrpc.aspect.FoxScheduleAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({FoxScheduleAspect.class})
public @interface EnableFoxScheduler {
    String packageName() default ""; //包名
}