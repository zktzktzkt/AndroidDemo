package demo.zkttestdemo.retrofit;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 无网络异常处理拦截器
 * Created by zkt on 2018-2-3.
 */

public class PreHandleNoNetIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetworkUtils.isConnected()) {
            chain.proceed(chain.request());
        } else {
            throw new ResultException(NetworkConfig.ERROR_CODE_NO_NET, "当前网络不通，请确认网络后重试");
        }
        return null;
    }
}
