package com.srwing.net;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Description:
 * Created by small small su
 * Date: 2022/8/19
 * Email: surao@foryou56.com
 */
public interface MainService {
    @GET("/api/engine/feeds/home/0086-golaxy_public")
    Observable<HomeListEntity> getHomeList(@QueryMap Map<String, Object> map);
}
