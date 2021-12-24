package com.srwing.newtwork.filter.data;

import com.srwing.newtwork.live.Status;

import java.io.Serializable;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
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
