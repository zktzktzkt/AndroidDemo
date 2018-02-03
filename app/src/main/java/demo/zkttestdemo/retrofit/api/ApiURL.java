package demo.zkttestdemo.retrofit.api;

import demo.zkttestdemo.retrofit.bean.ApiBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public interface ApiURL {
    @GET("geocoding")
    Observable<ApiBean> getApiBean(@Query("a") String city); //@Query是向后追加的效果 等同于 geocoding?a=city
}

//    @GET("group/users")
//    Call<List<User>> groupList(@Query("id") int groupId);
//--> http://baseurl/group/users?id=groupId
