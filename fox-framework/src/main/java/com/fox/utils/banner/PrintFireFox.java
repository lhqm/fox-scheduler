package com.fox.utils.banner;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 17:14
 */
@Slf4j
public class PrintFireFox {
    public static void printFireFox(){
        log.info("\n   __                        _   _         \n" +
                "  / _| _____  __  _ __   ___| |_| |_ _   _ \n" +
                " | |_ / _ \\ \\/ / | '_ \\ / _ \\ __| __| | | |\n" +
                " |  _| (_) >  <  | | | |  __/ |_| |_| |_| |\n" +
                " |_|  \\___/_/\\_\\ |_| |_|\\___|\\__|\\__|\\__, |\n" +
                "                                     |___/ \n" +
                "============锵锵！Netty启动成功！=========\n");
    }
}
