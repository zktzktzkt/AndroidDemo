package demo.zkttestdemo.effect.pullAndload;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;

import demo.zkttestdemo.R;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class MTPtrActivity extends Activity {

    private SpringView springview;
    private int[] pullAnimSrcs = new int[]{R.drawable.mt_pull,R.drawable.mt_pull01,R.drawable.mt_pull02,R.drawable.mt_pull03,R.drawable.mt_pull04,R.drawable.mt_pull05};
    private int[] refreshAnimSrcs = new int[]{R.drawable.mt_refreshing01,R.drawable.mt_refreshing02,R.drawable.mt_refreshing03,R.drawable.mt_refreshing04,R.drawable.mt_refreshing05,R.drawable.mt_refreshing06};
    private int[] loadingAnimSrcs = new int[]{R.drawable.mt_loading01,R.drawable.mt_loading02};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_load);

        springview = (SpringView) findViewById(R.id.springview);
        springview.setType(SpringView.Type.FOLLOW);
        springview.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springview.setHeader(new MeituanHeader(MTPtrActivity.this,pullAnimSrcs,refreshAnimSrcs));
                        springview.onFinishFreshAndLoad();
                        Toast.makeText(MTPtrActivity.this, "刷新", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MTPtrActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
                        springview.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
        });

        springview.setHeader(new MeituanHeader(this,pullAnimSrcs,refreshAnimSrcs));
        springview.setFooter(new MeituanFooter(this,loadingAnimSrcs));
    }
}
