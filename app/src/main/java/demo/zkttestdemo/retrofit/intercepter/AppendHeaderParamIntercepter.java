package demo.zkttestdemo.retrofit.intercepter;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 统一追加Header
 * Created by zkt on 2018-2-3.
 */

public class AppendHeaderParamIntercepter implements Interceptor {

    // 1.获取以前的Builder
    // 2.为以前的Builder添加参数
    // 3.生成新的Builder

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Headers.Builder builder = request
                .headers()
                .newBuilder();

        //统一追加Header参数
        Headers newBuilder = builder.add("header1", "i am header 1")
                .add("token", "i am token")
                .build();

        Request newRequest = request.newBuilder()
                .headers(newBuilder)
                .build();

        return chain.proceed(newRequest);
    }
}
