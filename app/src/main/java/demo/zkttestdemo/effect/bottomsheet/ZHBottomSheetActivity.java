package demo.zkttestdemo.effect.bottomsheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import demo.zkttestdemo.R;
import demo.zkttestdemo.utils.DisplayUtil;

public class ZHBottomSheetActivity extends AppCompatActivity {
    private static final String TAG = "ZHBottomSheetActivity";

    private BottomSheetBehavior<View> behavior;
    private TextView checkbox;
    boolean mChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        checkbox = (TextView) findViewById(R.id.checkbox);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChecked = !mChecked;
                checkbox.setSelected(mChecked);
            }
        });

        /**
         * setPeekHeight	偷看的高度；哈，这么理解，就是默认显示后View露头的高度
         * getPeekHeight	@see setPeekHeight()
         * setHideable	设置是否可以隐藏，如果为true,表示状态可以为STATE_HIDDEN
         * isHideable	@see setHideable()
         * setState	设置状态;设置不同的状态会影响BottomSheetView的显示效果
         * getState	获取状态
         * setBottomSheetCallback	设置状态改变回调
         *
         *
         * STATE_COLLAPSED： bottom sheets只在底部显示一部分布局。显示高度可以通过 app:behavior_peekHeight 设置（默认是0）
         * STATE_DRAGGING ： 过渡状态，此时用户正在向上或者向下拖动bottom sheet
         * STATE_SETTLING: 视图从脱离手指自由滑动到最终停下的这一小段时间
         * STATE_EXPANDED： bottom sheet 处于完全展开的状态：当bottom sheet的高度低于CoordinatorLayout容器时，整个bottom sheet都可见；或者CoordinatorLayout容器已经被bottom sheet填满。
         * STATE_HIDDEN ： 默认无此状态（可通过app:behavior_hideable 启用此状态），启用后用户将能通过向下滑动完全隐藏 bottom sheet
         */

        View bottomSheet = findViewById(R.id.bottom_sheet);
        // 1.拿到这个tab_layout对应的BottomSheetBehavior
        behavior = BottomSheetBehavior.from(bottomSheet);
        //设置折叠的高度
        behavior.setPeekHeight(DisplayUtil.dip2px(150, this));

        findViewById(R.id.btnBehavior).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2. 获取状态，是展开还是隐藏
                int state = behavior.getState();

                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN); // 被触发时，整个BottomSheet完全隐藏
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);// 被触发时，BottomSheet不会全部隐藏，会露出peekHeight的高度
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // 被触发时，完全展示
                }
            }
        });

        //bottomSheet状态监听
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            /**
             * 当bottom sheets状态被改变回调
             * @param bottomSheet The bottom sheet view.
             * @param newState    改变后新的状态
             */
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            /**
             * 当bottom sheets拖拽时回调
             * @param bottomSheet The bottom sheet view.
             * @param slideOffset 滑动量;从0到1时向上移动
             */
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.e(TAG, "slideOffset:" + slideOffset + "");

            }
        });

        /************************************BottomSheetDialog*******************************/
        final BottomSheetDialog bsDialog = new BottomSheetDialog(this);
        bsDialog.setContentView(R.layout.include_bottom_sheet_layout);

        findViewById(R.id.btnDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bsDialog.isShowing()) {
                    bsDialog.dismiss();
                } else {
                    bsDialog.show();
                }

            }
        });

        /************************************BottomSheetDialogFragment*******************************/
        final CustomBottomSheetDialogFragment dialogFragment = new CustomBottomSheetDialogFragment();

        findViewById(R.id.btnDialogFragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "Dialog");
            }
        });

       /* class BottomSheetDialogFragment extends AppCompatDialogFragment {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return new BottomSheetDialog(getActivity(), getTheme());
            }

        }*/

        /***********************************Popupwindow显示在某个布局上方*****************/
        findViewById(R.id.btnPopupOnTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ZHBottomSheetActivity.this, PopUpBottomSheetActivity.class));
            }
        });

        /***********************************Dialog显示在某个布局上方*****************/
        findViewById(R.id.btnDialogOnTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ZHBottomSheetActivity.this, DialogOnTopActivity.class));
            }
        });

    }


}
