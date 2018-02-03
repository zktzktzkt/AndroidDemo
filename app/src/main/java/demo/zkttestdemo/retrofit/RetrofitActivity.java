package demo.zkttestdemo.retrofit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import demo.zkttestdemo.R;
import demo.zkttestdemo.retrofit.api.ApiURL;
import demo.zkttestdemo.retrofit.api.GitHubApi;
import demo.zkttestdemo.retrofit.intercepter.AppendHeaderParamIntercepter;
import demo.zkttestdemo.retrofit.intercepter.AppendUrlParamIntercepter;
import demo.zkttestdemo.retrofit.bean.ApiBean;
import demo.zkttestdemo.retrofit.bean.Contributor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class RetrofitActivity extends Activity implements View.OnClickListener {

    private Button btn_get;
    private Button btn_get_1;
    private Button btn_commitFile;
    private Button btn_downFile;
    private Toolbar toolbar;
    private String mUserName = "zktzktzkt";
    private String mRepo = "retrofit";
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        btn_get = (Button) findViewById(R.id.btn_get);
        btn_get_1 = (Button) findViewById(R.id.btn_get_1);
        btn_commitFile = (Button) findViewById(R.id.btn_commitFile);
        btn_downFile = (Button) findViewById(R.id.btn_downFile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_content = (TextView) findViewById(R.id.tv_content);
        initToolbar();
        btn_get.setOnClickListener(this);
        btn_get_1.setOnClickListener(this);
    }


    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ico_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                //getNotUseConverter();
                getUseConverter();
                break;
            case R.id.btn_get_1:
                getDataByRx();
                break;
        }
    }

    /**
     * GET 使用converter 解析-------------------------------------
     */
    private void getUseConverter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubApi repo = retrofit.create(GitHubApi.class);
        Call<Contributor> call = repo.contributorsBySimpleGetCall1(mUserName);
        call.enqueue(new Callback<Contributor>() {
            @Override
            public void onResponse(Call<Contributor> call, Response<Contributor> response) {
                Contributor contributor = response.body();
                tv_content.setText(contributor + "");
            }

            @Override
            public void onFailure(Call<Contributor> call, Throwable t) {

            }
        });
    }

    /**
     * GET  不使用converter-----------------------------------------------------------------------
     */
    private void getNotUseConverter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build();
        GitHubApi repo = retrofit.create(GitHubApi.class);
        Call<ResponseBody> call = repo.contributorsBySimpleGetCall(mUserName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                  /*  ArrayList<Contributor> contributorsList = gson.fromJson(response.body().string(), new TypeToken<List<Contributor>>() {
                    }.getType());*/
                    Contributor contributor = gson.fromJson(response.body().string(), Contributor.class);

                    tv_content.setText(contributor.toString());

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getDataByRx() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加拦截器，自动追加参数
        builder.addInterceptor(new AppendUrlParamIntercepter());
        builder.addInterceptor(new AppendHeaderParamIntercepter());
        //添加拦截器，打印网络请求
        if (NetworkConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gc.ditu.aliyun.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();
        ApiURL apiURL = retrofit.create(ApiURL.class);
        Observable<ApiBean> api = apiURL.getApiBean("北京市");

        api.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiBean>() {
                    @Override
                    public void accept(ApiBean apiBean) throws Exception {

                    }
                });
    }

}
