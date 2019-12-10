package com.security.securitylearn.common;

import java.io.Serializable;

public class RestBody<T> implements Rest<T>, Serializable {

    private static final long serialVersionUID = -7616216747521482608L;
    private int httpStatus = 200;
    private T data;
    private String msg = "";
    private String identifier = "";

    public int getHttpStatus() {
        return httpStatus;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public void setMsg(String message) {
        this.msg = message;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public static Rest ok() {
        return new RestBody();
    }

    public static Rest ok(String msg) {
        Rest restBody = new RestBody();
        restBody.setMsg(msg);
        return restBody;
    }

    public static <T> Rest<T> okData(T data) {
        Rest<T> restBody = new RestBody<>();
        restBody.setData(data);
        return restBody;
    }

    public static <T> Rest<T> okData(T data, String msg) {
        Rest<T> restBody = new RestBody<>();
        restBody.setData(data);
        restBody.setMsg(msg);
        return restBody;
    }

    /**
     *
     * @param httpStatus http状态码
     * @param data       数据
     * @param msg        信息
     * @param identifier 标识
     * @param <T>        类型参数
     * @return
     */
    public static <T> Rest<T> build(int httpStatus, T data, String msg, String identifier) {
        Rest<T> rest = new RestBody<>();
        rest.setHttpStatus(httpStatus);
        rest.setData(data);
        rest.setMsg(msg);
        rest.setIdentifier(identifier);
        return rest;
    }


    public static Rest failure(int httpStatus, String msg) {
        Rest restBody = new RestBody();
        restBody.setHttpStatus(httpStatus);
        restBody.setMsg(msg);
        restBody.setIdentifier("-9999");
        return restBody;
    }

    public static <T> Rest<T> failure(T data, String msg, String identifier) {
        Rest<T> restBody = new RestBody<>();
        restBody.setMsg(msg);
        restBody.setIdentifier(identifier);
        restBody.setData(data);
        return restBody;
    }

    @Override
    public String toString() {
        return "{" +
                "httpStatus:" + httpStatus +
                ", data:" + data +
                ", msg:" + msg +
                ", identifier:" + identifier +
                '}';
    }
}
