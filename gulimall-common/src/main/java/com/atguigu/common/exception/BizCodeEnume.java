package com.atguigu.common.exception;

/**
 * @author weepppp 2022/6/29 19:33
 * @version V1.0
 **/
public enum BizCodeEnume {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数校验失败"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常");

    private Integer code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
