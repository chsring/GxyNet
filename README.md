# GxyNet
项目的build.gradle

 ```groovy
  allprojects {
		  repositories {
			  //...
			  maven { url 'https://jitpack.io' }
		  }
	}
```
  
app下build.gradle

 ```groovy
	dependencies {
	        implementation 'com.github.chsring:GxyNet:v1.1.2'
	}
```

Application中 初始化配置
```java
    private void initNetConfig() {
        GxyNet.init(this)
        .withApiHost(BaseUrl)
        .withInterceptor(new LogInterceptor())
        .withLoggerAdapter() //设置LogAdapter
        .withDebugMode(true) //设置是否打印请求 日志
        .withNoProxy(false)
        .configure(); //配置生效

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
         GxyNet.builder()
                .service(DanCiService.class)
                .params(DanCiListActivity.CIXING_TYPE,ciXing)
                .method((IMethod<DanCiService>) DanCiService::getDanCi)
                .success((ISuccess<DanCiEntity>) callback::onTaskLoaded)
                .failure((callback::onDataNotAvailable))
                .build()
                .excute();
```

