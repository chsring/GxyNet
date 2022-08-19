package com.srwing.t_network.filter.chain;

import com.srwing.t_network.filter.RespFilter;
import com.srwing.t_network.filter.data.RespData;

import java.util.List;

/**
 * @author srwing
 * @fileName RealRespChain
 * @time 2022/6/17 2:31 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public class RealRespChain implements RespFilter.RespChain {


    private final List<RespFilter> filters;
    private final RespData respData;
    private final int index;

    public RealRespChain(List<RespFilter> filters, RespData respData, int index) {
        this.filters = filters;
        this.respData = respData;
        this.index = index;
    }

    @Override
    public RespData proceed(RespData data) throws Exception {

        if (index >= filters.size()) {
            return data;
        }
        RespFilter.RespChain next = new RealRespChain(filters, respData, index + 1);
        RespFilter respFilter = filters.get(index);
        RespData respData = respFilter.filter(next);

        return respData;
    }

    @Override
    public RespData respData() {
        return respData;
    }


}
