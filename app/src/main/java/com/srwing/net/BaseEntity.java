package com.srwing.net;

/**
 * Description:
 * Created by srwing
 * Date: 2022/6/20
 * Email: 694177407@qq.com
 */
public class BaseEntity {
    public int code;
    public String msg;
    public String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
