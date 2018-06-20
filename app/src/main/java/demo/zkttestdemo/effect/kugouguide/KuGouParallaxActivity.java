package demo.zkttestdemo.effect.kugouguide;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.kugouguide.parallax.ParallaxViewPager;

/**
 * 酷狗新手引导页
 */
public class KuGouParallaxActivity extends AppCompatActivity {
    private ParallaxViewPager mParallaxVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ku_gou_parallax);

        mParallaxVp = findViewById(R.id.parallax_vp);

        //给一个方法
        mParallaxVp.setLayout(getSupportFragmentManager(),
                new int[]{R.layout.fragment_page_first, R.layout.fragment_page_second, R.layout.fragment_page_third});

        asyncTask.execute();

    }

    @SuppressLint("StaticFieldLeak")
    AsyncTask asyncTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    };



}
