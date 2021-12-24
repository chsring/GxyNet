package com.srwing.newtwork.filter;

import com.srwing.newtwork.filter.data.RespData;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
 */
public interface RespFilter {

    RespData filter(RespChain chain) throws Exception;

    interface RespChain {

        RespData proceed(RespData data) throws Exception;

        RespData respData();
    }

}
