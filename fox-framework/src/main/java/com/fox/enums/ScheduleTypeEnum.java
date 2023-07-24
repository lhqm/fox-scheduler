package com.fox.enums;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/24 11:08
 */
public enum ScheduleTypeEnum {
    DELAY("delay",""),//延迟执行。该执行的时长单位为秒，基于delayQueue。在执行时间非特殊的情况下，更推荐使用下述的秒、分、时等带单位的选项进行执行
    CRON("cron",""),//使用cron表达式进行执行
    ONCE("once",""),//一次执行。该选项将按照延时器执行相关代码，并且在执行之后对该代码进行舍弃
    SECONDLY("secondly","* * * * * ?"),//以秒单位执行
    MINUTELY("minutely","0 * * * * ?"),//以分钟为单位执行
    HOURLY("hourly","0 0 * * * ?"),//以小时为单位执行
    DAILY("daily","0 0 0 * * ?"),//以天为单位执行
    WEEKLY("weekly","0 0 0 ? * SUN"),//以周为单位执行(默认星期天)
    MONTHLY("monthly","0 0 0 1 * ? *"),//以月为单位执行
    YEARLY("yearly","0 0 0 1 1 ? *"),//以年为单位执行
    ;
    private String type;
    private String cronString;

    ScheduleTypeEnum(String type,String cronString){
        this.type=type;
        this.cronString=cronString;
    }
    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getCronString(){
        return this.cronString;
    }
    public void setCronString(String cronString){
        this.cronString=cronString;
    }


}
