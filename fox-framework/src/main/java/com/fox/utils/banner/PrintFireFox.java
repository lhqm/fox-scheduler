package com.fox.utils.banner;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 17:14
 */
@Slf4j
public class PrintFireFox {
    /**
     * 服务端程序启动使用
     */
    public static void printServer(){
        log.info("\n" +
                "  _____             ____                           \n" +
                " |  ___|____  __   / ___|  ___ _ ____   _____ _ __ \n" +
                " | |_ / _ \\ \\/ /   \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|\n" +
                " |  _| (_) >  <     ___) |  __/ |   \\ V /  __/ |   \n" +
                " |_|  \\___/_/\\_\\   |____/ \\___|_|    \\_/ \\___|_|   \n" +
                "============锵锵！Netty启动成功！=========\n");
    }
    /**
     * 服务端程序启动使用
     */
    public static void printClient(){
        log.info("\n" +
                "  _____              ____ _ _            _   \n" +
                " |  ___|____  __    / ___| (_) ___ _ __ | |_ \n" +
                " | |_ / _ \\ \\/ /   | |   | | |/ _ \\ '_ \\| __|\n" +
                " |  _| (_) >  <    | |___| | |  __/ | | | |_ \n" +
                " |_|  \\___/_/\\_\\    \\____|_|_|\\___|_| |_|\\__|\n" +
                "============哒哒！客户端启动成功！=========\n");
    }
}
