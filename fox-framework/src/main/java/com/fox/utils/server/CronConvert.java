package com.fox.utils.server;

import com.fox.enums.ScheduleTypeEnum;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/24 16:19
 * cron仲裁。
 * 通过落在目标范围内的cron类型，来仲裁得到cron表达式。
 * 一般是指每天、每周、每个月等触发的cron任务
 */
public class CronConvert {
    public static String crontabConvert(String target){
        //获取到目标枚举类
        ScheduleTypeEnum scheduleType = ScheduleTypeEnum.valueOf(target.toUpperCase());
        //获取到枚举类对应的cron表达式
        return scheduleType.getCronString();
    }
}
