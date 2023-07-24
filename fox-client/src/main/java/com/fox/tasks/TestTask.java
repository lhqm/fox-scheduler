package com.fox.tasks;

import com.fox.annotation.FoxSchedule;
import com.fox.enums.ScheduleTypeEnum;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/24 13:55
 */
public class TestTask {
    @FoxSchedule(taskName = "测试调度方法1",type = ScheduleTypeEnum.CRON,cron="0 0/30 * * * *")
    public void testTask1(){
        System.out.println("hello");
    }
    @FoxSchedule(taskName = "测试调度方法2",type = ScheduleTypeEnum.ONCE,delay = 1000)
    public void testTask2(){
        System.out.println("hello");
    }
    @FoxSchedule(taskName = "测试调度方法3",type = ScheduleTypeEnum.DAILY,twice = 2)
    public void testTask3(){
        System.out.println("hello");
    }
}
