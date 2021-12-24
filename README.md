# bynet
项目的build.gradle

 ```java
  allprojects {
		  repositories {
			  ...
			  maven { url 'https://jitpack.io' }
		  }
	}
```
  
app下build.gradle

 ```java
	dependencies {
	        implementation 'com.github.chsring:bynet:Tag'
	}
```

Application中 初始化配置
```java
    private void initNetConfig() {

        Configurator configurator = ByNet.init(mApp)  //初始化
                .withApiHost(UrlConstant.URL_BASE);  //设置网络请求 同理的 Domain
        if (BuildConfig.DEBUG) {
            configurator.withInterceptor(new ByInterceptor()); //设置请求日志解析拦截器
            configurator.withInterceptor(new ErrorLogInterceptor()); //设置请求错误处理拦截器
        }
        configurator.withInterceptor(new LogInterceptor());
        configurator.withRespFilter(new FyResponseFilter()); //设置网络请求返回值 解析Filter
        configurator.withRespFilter(new UpgradeFilter());  //强制升级 filter
        configurator.withToken(UserManager.get().getToken()); //这只全局请求token
        configurator.withNetGlobleParams(ApnInit.getHeads());   //这只全局请求参数
        configurator.withLoggerAdapter();  //设置LogAdapter
        configurator.withDebugMode(BuildConfig.DEBUG);  //设置是否打印请求 日志
        configurator.withNoProxy(false);
        configurator.configure();  //配置生效

    }
 ```
 使用方法:
 
 1. 注册接口
 ```java
public interface DanCiService {

    //get请求
    @GET("word/getWordByCiXing")
    Observable<DanCiEntity> getDanCi(@QueryMap WeakHashMap<String, Object> params);

    //post请求
    @FormUrlEncoded
    @POST("word/getWordByCiXing")
    Observable<DanCiEntity> getDanCiPost(@FieldMap WeakHashMap<String, Object> params);

}
```

 2. 发起请求
 ```java
         ByNet.builder()
                .service(DanCiService.class)
                .params(DanCiListActivity.CIXING_TYPE,ciXing)
                .method((IMethod<DanCiService>) DanCiService::getDanCi)
                .success((ISuccess<DanCiEntity>) callback::onTaskLoaded)
                .failure((callback::onDataNotAvailable))
                .build()
                .excute();
```

