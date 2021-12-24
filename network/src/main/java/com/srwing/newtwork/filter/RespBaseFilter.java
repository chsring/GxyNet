package com.srwing.newtwork.filter;

import com.srwing.newtwork.filter.data.RespData;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
 */
public class RespBaseFilter implements RespFilter {

    @Override
    public RespData filter(RespChain chain) throws Exception {

        RespData respData = chain.respData();
        RespData dealRespData = chain.proceed(respData);
        if (dealRespData != null) {
            respData = dealRespData;
        }
        return respData;
    }

}
