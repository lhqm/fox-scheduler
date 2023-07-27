package com.fox.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 9:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientData implements Serializable {
//    调度方案集合
    private List<TaskedMethod> taskedMethods;
//    调度客户端名称
    private String clientName;

    //    反序列化构造器
    public static ClientData fromJsonString(String jsonString) {
        JSONObject json = JSONObject.parseObject(jsonString);
//        反序列化对象
        ClientData clientData = new ClientData();
        clientData.setClientName(json.getString("clientName"));
//        序列化列表
        JSONArray methods = json.getJSONArray("taskedMethods");
        List<TaskedMethod> taskedMethodList=new LinkedList<>();
        methods.forEach(item->{
            JSONObject task = (JSONObject) item;
            taskedMethodList.add(TaskedMethod.fromJsonString(task.toJSONString()));
        });
        clientData.setTaskedMethods(taskedMethodList);
        return clientData;
    }
}
