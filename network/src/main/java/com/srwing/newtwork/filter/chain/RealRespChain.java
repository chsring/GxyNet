package com.srwing.newtwork.filter.chain;

import com.srwing.newtwork.filter.RespFilter;
import com.srwing.newtwork.filter.data.RespData;

import java.util.List;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
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
