package demo.zkttestdemo.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.utils.LogUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class RxJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
    }

    public void justClick(View view) {
        Flowable.just("hello world").subscribe(new FlowableSubscriber<String>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {

            }

            @Override
            public void onNext(String s) {
                LogUtil.e(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void action1Click(View view) {
        Flowable.just(nowTime()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                LogUtil.e(s);
            }
        });
    }


    private static String nowTime() {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sim.format(System.currentTimeMillis());
        return time;
    }

    public void mapClick(View view) {
        //function有返回值， Consumer无返回值

        Flowable.just("map ")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return s + nowTime();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        LogUtil.e(s);
                    }
                });
        //--------------------------------------------------

        Flowable.just("map1 ")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(@NonNull String s) throws Exception {
                        return s.hashCode();
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        LogUtil.e(integer+"");
                    }
                });

    }

    //--------------------------------------

    public void justListClick(View view) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(1);
        list.add(5);

        Flowable.just(list)
                .flatMap(new Function<List<Integer>, Publisher<Integer>>() {  //flatmap作用就是为了对接收的数据再进行发射
                    @Override
                    public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
                        return Flowable.fromIterable(integers);
                    }
                })
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer >= 5;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        LogUtil.e(integer + "");
                    }
                });

    }

    //--------------------------
    public void fromArrayClick(View view) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(1);
        list.add(3);
        list.add(7);
        list.add(4);
        Flowable.fromArray(list)
                .flatMap(new Function<List<Integer>, Publisher<Integer>>() {
                    @Override
                    public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
                        return Flowable.fromIterable(integers);
                    }
                })
                .take(2) //只取前两个
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        LogUtil.e(integer + "");
                    }
                });
    }


    public void doOnNextClick(View view) {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(1);
        list.add(3);
        list.add(7);
        list.add(4);
        Flowable.just(list)
                .flatMap(new Function<List<Integer>, Publisher<Integer>>() {
                    @Override
                    public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
                        return Flowable.fromIterable(integers);
                    }
                })
                .doOnNext(new Consumer<Integer>() {  //doOnNext 允许我们在每次输出一个元素之前做一些额外的事情。
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {

                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        LogUtil.e(integer + "");
                    }
                });
    }

    public void io(View view){
        Flowable.just("121212")
                .map(new Function<String, Long>() {
                    @Override
                    public Long apply(String s) throws Exception {
                        return Long.parseLong(s);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long s) throws Exception {
                        LogUtil.e(s+"");
                    }
                });
    }
}
