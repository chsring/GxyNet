package com.srwing.t_network.filter.data;

import com.srwing.t_network.live.Status;

import java.io.Serializable;

/**
 * @author srwing
 * @fileName RespData
 * @time 2022/6/17 2:33 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public class RespData<EntityType> implements Serializable {

    public IRespEntity<EntityType> respEntity;
    public IRespError<EntityType> respError;
    public int succCode;

    public int succCode() {
        return succCode;
    }
    public int dealCode() {
        return respError.code();
    }

    public int errorCode() {
        return respError.code();
    }

    public String errorMsg() {
        return respError.errorMsg();
    }

    public EntityType entity() {
        return respEntity.entity();
    }

    public Status status;


}
