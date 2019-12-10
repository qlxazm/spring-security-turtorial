package com.security.securitylearn.common;

public interface Rest<T> {

    /**
     * 设置响应的状态码
     * @param httpStatus
     */
    void setHttpStatus(int httpStatus);

    /**
     * 数据载体
     * @param data
     */
    void setData(T data);

    /**
     * 设置提示信息
     * @param message
     */
    void setMsg(String message);

    /**
     * 设置标识
     * @param identifier
     */
    void setIdentifier(String identifier);
}
