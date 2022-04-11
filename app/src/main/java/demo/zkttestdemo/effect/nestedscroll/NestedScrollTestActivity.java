package demo.zkttestdemo.effect.nestedscroll;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.SizeUtils;

import demo.zkttestdemo.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 嵌套滑动测试
 * <p>
 * 方式1、【推荐】NestedScroll机制，parent和child协调消费滑动距离
 * （不管是通过CoordinatorLayout+behavior或实现NestedScrollParent/child接口都可以）
 * <p>
 * 方式2、必须先触摸可滚动的child，通过手势的偏移量，对child和其它View做translationY。
 */
public class NestedScrollTestActivity extends AppCompatActivity {

    private int c1Height;
    private int rootHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll_test);

        final CustomPtrClassicFrameLayout ptr = findViewById(R.id.ptr);
        final RelativeLayout rootView = findViewById(R.id.rootView);
        final LinearLayout ll_c1 = findViewById(R.id.ll_c1);
        final CustomNestedScrollView nsv = findViewById(R.id.nsv);

        int maxY = SizeUtils.dp2px(40);

        nsv.setDispatchTouchEventListener(new CustomNestedScrollView.DispatchTouchEventListener() {
            @Override
            public boolean down() {
                return false;
            }

            @Override
            public void move(float moveY, float distance) {
                //上滑
                if (moveY < 0) {
                    if (ll_c1.getTranslationY() > -maxY) {
                        ll_c1.setTranslationY(ll_c1.getTranslationY() + moveY);
                        nsv.setTranslationY(nsv.getTranslationY() + moveY);
                        //边界限制
                        if (ll_c1.getTranslationY() < -maxY) {
                            ll_c1.setTranslationY(-maxY);
                            nsv.setTranslationY(-maxY);
                        }
                    }
                }
                //下滑
                else {
                    if (nsv.getScrollY() != 0 || ll_c1.getTranslationY() != 0) {
                        ptr.setEnabled(false);
                    }
                    if (ll_c1.getTranslationY() < 0) {
                        ll_c1.setTranslationY(ll_c1.getTranslationY() + moveY);
                        nsv.setTranslationY(nsv.getTranslationY() + moveY);
                        //边界限制
                        if (ll_c1.getTranslationY() >= 0) {
                            ll_c1.setTranslationY(0);
                            nsv.setTranslationY(0);
                        }
                    }
                }
            }
        });

        rootView.post(new Runnable() {
            @Override
            public void run() {
                //最外层的布局高度
                rootHeight = rootView.getMeasuredHeight();
                //第一个布局的高度
                c1Height = ll_c1.getMeasuredHeight();
                //获取剩余空间，设置给NSV
                int height = rootHeight - c1Height;
                ViewGroup.LayoutParams params = nsv.getLayoutParams();
                params.height = height;
                nsv.setLayoutParams(params);
            }
        });

        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("scrollview滑动测试", scrollY + "");
                ptr.setEnabled(scrollY == 0 && ll_c1.getTranslationY() == 0);
            }
        });

        ptr.post(() -> {
            ptr.setHeaderView(getLayoutInflater().inflate(R.layout.headerview_default, null));
            ptr.setShowHeader(true);
            // ptr.setShowHeaderHideDistance(100);
        });
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptr.refreshComplete();
            }
        });

    }
}
