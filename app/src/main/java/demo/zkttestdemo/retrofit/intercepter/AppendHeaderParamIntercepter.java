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

        // 获取“请求头”的Builder，统一追加Header参数，设置值
        Headers newBuilder = request.headers().newBuilder()
                .add("header1", "i am header 1")
                .add("token", "i am token")
                .build();

        // 获取“请求”的Builder，设置新的Header
        Request newRequest = request.newBuilder()
                .headers(newBuilder)
                .build();

        return chain.proceed(newRequest); //执行请求
    }
}
