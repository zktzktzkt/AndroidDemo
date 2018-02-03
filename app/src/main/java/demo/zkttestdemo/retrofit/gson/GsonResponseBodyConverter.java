package demo.zkttestdemo.retrofit.gson;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.Type;

import demo.zkttestdemo.retrofit.ResultException;
import demo.zkttestdemo.retrofit.bean.BaseBean;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //value 服务器回传给我们的body
        String response = value.toString();

        //构建泛型的Type  BaseBean<type>
        Type baseBeanType = $Gson$Types.newParameterizedTypeWithOwner(null, BaseBean.class, type);
        BaseBean baseBean = gson.fromJson(response, baseBeanType);
        if (baseBean.isError()) {
            throw new ResultException(baseBean.getErrCode(), baseBean.getErrMsg());
        } else {
            return (T) baseBean;
        }
    }
}
