package demo.zkttestdemo.retrofit.intercepter;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Get请求自动追加参数
 * Created by zkt on 2018-2-3.
 */

public class AppendUrlParamIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //偷天换日
        Request oldRequest = chain.request();

        //拿到拥有以前的request里的url的那些信息的builder
        HttpUrl.Builder builder = oldRequest
                .url()
                .newBuilder();

        //得到新的url（已经追加好了参数）
        HttpUrl newUrl = builder.addQueryParameter("deviceId", "12345")
                .addQueryParameter("token", "i_am_token")
                .addQueryParameter("appVersion", "1.0.0-beta")
                .build();

        //利用新的Url，构建新的request，并发送给服务器
        Request newRequest = oldRequest
                .newBuilder()
                .url(newUrl)
                .build();

        return chain.proceed(newRequest);
    }
}
