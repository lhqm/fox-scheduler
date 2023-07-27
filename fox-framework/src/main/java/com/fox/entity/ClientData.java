package com.fox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 9:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientData {
//    调度方案集合
    private List<TaskedMethod> taskedMethods;
//    调度客户端名称
    private String clientName;
}
