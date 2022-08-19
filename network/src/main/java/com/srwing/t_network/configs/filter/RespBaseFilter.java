package com.srwing.t_network.configs.filter;

import com.srwing.t_network.configs.filter.data.RespData;

/**
 * @author srwing
 * @fileName RespBaseFilter
 * @time 2022/6/17 2:33 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
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
