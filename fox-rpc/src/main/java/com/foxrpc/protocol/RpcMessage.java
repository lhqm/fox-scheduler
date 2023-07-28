package com.foxrpc.protocol;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcMessage {
//    定义请求ID
    private String requestId;
//    定义请求的服务名称（class）
    private String serviceName;
//    定义请求方法
    private String methodName;
//    定义序列化的请求参数
    private JSONObject parameters;
//    定义响应结果
    private JSONObject result;
//    定义错误信息（类似HTTP消息，有错误码和错误信息）
    private RpcError error;

//    序列化构造器
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("serviceName", serviceName);
        json.put("methodName", methodName);
        json.put("parameters", parameters);
        json.put("result", result);
        json.put("error", error);
        return json.toJSONString();
    }

//    反序列化构造器
    public static RpcMessage fromJsonString(String jsonString) {
        JSONObject json = JSONObject.parseObject(jsonString);
        RpcMessage rpcMessage = new RpcMessage();
        rpcMessage.setRequestId(json.getString("requestId"));
        rpcMessage.setServiceName(json.getString("serviceName"));
        rpcMessage.setMethodName(json.getString("methodName"));
        rpcMessage.setParameters(json.getJSONObject("parameters"));
        rpcMessage.setResult(json.getJSONObject("result"));
        rpcMessage.setError(json.getObject("error", RpcError.class));
        return rpcMessage;
    }

//    错误消息定义集合
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RpcError {
        private int code;
        private String message;

        @Override
        public String toString() {
            return "RpcError{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}