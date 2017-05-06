package demo.zkttestdemo.retrofit.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import demo.zkttestdemo.retrofit.bean.ApiBean;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public interface ApiURL {
    @GET("geocoding")
    Call<ApiBean> getApiBean(@Query("a") String city); //@Query把它看成一个键值对，相当于 ?a=city ,如果更多的参数：?a=city&b=haha&c=qwer
}

//    @GET("group/users")
//    Call<List<User>> groupList(@Query("id") int groupId);
//--> http://baseurl/group/users?id=groupId
