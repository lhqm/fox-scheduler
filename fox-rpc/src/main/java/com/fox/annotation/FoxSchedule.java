package com.fox.annotation;

import com.fox.enums.ScheduleTypeEnum;

import java.lang.annotation.*;

/**
 * 仅仅作为默认值赋给。该修改将仅仅应用于初始化。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FoxSchedule {
    String taskName() default "";//执行的任务名称
    ScheduleTypeEnum type() default ScheduleTypeEnum.ONCE;//执行类型(默认执行一次)
    String cron() default "";//cron表达式。仅当当前类型为cron时触发
    int delay() default 0;//执行延时，默认立即执行
    int twice() default -1;//执行次数。-1为永久执行。如果执行类型为ONCE，那么该选项设置后不可用
    boolean fresh() default false;//是否将初始化信息作为刷新依据，刷新到服务端中央调度库
}