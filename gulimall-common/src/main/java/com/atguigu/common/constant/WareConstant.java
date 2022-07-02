package com.atguigu.common.constant;

/**
 * @author weepppp 2022/7/2 11:20
 * @version V1.0
 **/
public class WareConstant {
    public enum PurchaseStausEnum{
        CREATED(0,"新建"),
        ASSIGEND(1,"已分配"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
       HASERROR(4,"有异常");
        private int code;
        private String msg;
        PurchaseStausEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum PurchaseDetailStausEnum{
        CREATED(0,"新建"),
        ASSIGEND(1,"已分配"),
        BUYING(2,"正在采购"),
        FINISH(3,"已完成"),
        HASERROR(4,"采购失败");
        private int code;
        private String msg;
        PurchaseDetailStausEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
