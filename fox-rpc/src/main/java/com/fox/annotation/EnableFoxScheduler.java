package com.fox.annotation;

import com.fox.aspect.ScheduleAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ScheduleAspect.class)
public @interface EnableFoxScheduler {
    String packageName() default ""; //包名
}