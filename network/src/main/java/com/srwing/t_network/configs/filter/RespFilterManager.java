package com.srwing.t_network.configs.filter;

import com.srwing.t_network.configs.configs.ConfigKeys;
import com.srwing.t_network.configs.configs.Configurator;
import com.srwing.t_network.configs.filter.chain.RealRespChain;
import com.srwing.t_network.configs.filter.data.RespData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author srwing
 * @fileName RespFilterManager
 * @time 2022/6/17 2:34 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
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
