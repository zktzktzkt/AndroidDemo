package demo.zkttestdemo.retrofit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import demo.zkttestdemo.R;
import demo.zkttestdemo.retrofit.api.ApiURL;
import demo.zkttestdemo.retrofit.api.GitHubApi;
import demo.zkttestdemo.retrofit.bean.ApiBean;
import demo.zkttestdemo.retrofit.bean.Contributor;
import demo.zkttestdemo.retrofit.gson.CstGsonConverterFactory;
import demo.zkttestdemo.retrofit.intercepter.AppendHeaderParamIntercepter;
import demo.zkttestdemo.retrofit.intercepter.AppendUrlParamIntercepter;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

    @SuppressLint("CheckResult")
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

        /**
         * 1. 每调一个操作符方法，其内部就会创建对应的操作符对象，并且把调用者传给创建的操作符对象
         * 2. 整个流程是对象套对象，A->B, (A->B)->C, (A->B->C)->D
         * 3. 当最后一个操作符对象调用subscribe，这时就会不断往上调，调用上一个对象的subscribe
         * 4. 调用到最顶的时候，如果成功了，就会往下不断调用onSuccess，直至用户自定义的Observer
         */
        //创建SingleJust
        Single<Integer> singleJust = Single.just(1);
        //创建SingleFlatMap，同时传入SingleJust
        Single<String> singleMap = singleJust.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return String.valueOf(integer);
            }
        });
        singleMap.subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(String o) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
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

    /**
     * RxJava + Retrofit
     */
    @SuppressLint("CheckResult")
    private void getDataByRx() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // “按照顺序” 添加拦截器，自动追加参数
        builder.addInterceptor(new PreHandleNoNetIntercepter()); // 异常
        builder.addInterceptor(new AppendUrlParamIntercepter()); // 追加Url
        builder.addInterceptor(new AppendHeaderParamIntercepter()); // 追加Header
        //添加拦截器，打印网络请求
        if (NetworkConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gc.ditu.aliyun.com/")
                // .addConverterFactory(GsonConverterFactory.create())
                // 加入我们自定义的Gson解析库，就可以更友好的处理错误
                .addConverterFactory(CstGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(builder.build())
                .build();

        Single<ApiBean> api = retrofit.create(ApiURL.class).getApiBean("北京市");
        api.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ApiBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ApiBean apiBean) {
                        Toast.makeText(RetrofitActivity.this, "成功了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RetrofitActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
