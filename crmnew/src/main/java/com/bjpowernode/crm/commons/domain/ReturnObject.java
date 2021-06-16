package com.bjpowernode.crm.commons.domain;

/**
 * 2021/4/25
 */
public class ReturnObject {

    private String code; //1 成功  0 失败
    private String message;
    private Object retData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
