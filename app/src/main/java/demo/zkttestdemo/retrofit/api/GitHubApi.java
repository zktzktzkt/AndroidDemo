package demo.zkttestdemo.retrofit.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import demo.zkttestdemo.retrofit.bean.Contributor;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public interface GitHubApi {

    @GET("users/{userName}")
    Call<ResponseBody> contributorsBySimpleGetCall(@Path("userName")String userName);

    @GET("users/{userName}")
    Call<Contributor> contributorsBySimpleGetCall1(@Path("userName")String userName);
/*
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributorsBySimpleGetCall2(@Path("owner")String owner, @Path("repo")String repo);*/
}
