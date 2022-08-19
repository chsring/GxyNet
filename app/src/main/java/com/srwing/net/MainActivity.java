package com.srwing.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.srwing.t_network.GxyNet;
import com.srwing.t_network.callback.IFailure;
import com.srwing.t_network.callback.ISuccess;
import com.srwing.t_network.http.IMethod;
import com.srwing.t_network.utils.GxyLogger;

import java.util.Map;
import java.util.WeakHashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, Object> map = new WeakHashMap<>();
        map.put("page", 1);
        map.put("size", 15);

                GxyNet.builder()
                        .params(map)
                        .service(MainService.class)
                        .method((IMethod<MainService>) MainService::getHomeList)
                        .success(new ISuccess<HomeListEntity>(){
                            @Override
                            public void onSuccess(HomeListEntity response) {
                                GxyLogger.d("success : " + response.code);
                            }
                        })
                        .failure(new IFailure() {
                            @Override
                            public void onFailure(int code, String desc) {
                                GxyLogger.d("code : " + code + "; desc: "+desc);
                            }
                        })
                        .build()
                        .excute();
    }
}