package com.srwing.newtwork.filter;

import com.srwing.newtwork.configs.ConfigKeys;
import com.srwing.newtwork.configs.Configurator;
import com.srwing.newtwork.filter.data.RespData;
import com.srwing.newtwork.filter.chain.RealRespChain;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by srwing
 * Date: 10/1/2019
 * Email: surao@foryou56.com
 */
public class RespFilterManager {

    /**
     * 执行 网络请求 返回数据 的分类处理 解耦框架和业务层
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static <T> RespData<T> execute(RespData<T> data) throws Exception {

        List<RespFilter> filters = new ArrayList<>();
        List<RespFilter> clientFilters = Configurator.getInstance().getConfiguration(ConfigKeys.NET_FILTER);

        filters.add(new RespBaseFilter());
        if (null != clientFilters && clientFilters.size() > 0) {
            filters.addAll(clientFilters);
        }
        RespFilter.RespChain realRespChain = new RealRespChain(filters, data, 0);
        return realRespChain.proceed(data);
    }


}
