package com.srwing.t_network.filter;

import com.srwing.t_network.filter.data.RespData;

/**
 * @author srwing
 * @fileName RespFilter
 * @time 2022/6/17 2:33 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public interface RespFilter {

    RespData filter(RespChain chain) throws Exception;

    interface RespChain {

        RespData proceed(RespData data) throws Exception;

        RespData respData();
    }

}
