package com.foxrpc.client;

import com.fox.entity.TaskedMethod;

import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/25 11:10
 * 让RPC模块来实现该接口。通过跨模块调用来注册客户端信息
 */
public interface InitInvocation {
    void sendTasks(List<TaskedMethod> taskedMethods, String addr, Integer port,String name);
}
