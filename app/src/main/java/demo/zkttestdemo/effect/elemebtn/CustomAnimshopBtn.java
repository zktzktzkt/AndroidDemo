package demo.zkttestdemo.effect.elemebtn;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by zkt on 2018-4-8.
 */

public class CustomAnimshopBtn extends AnimShopBtn {
    public CustomAnimshopBtn(Context context) {
        super(context);
    }

    public CustomAnimshopBtn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAnimshopBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 当 - 事件触发 回调
     */
    @Override
    protected void onDelClick() {
        if (mCount > 0) {
            mCount--;
            onDelSuccessListener();
            invalidate();
            if (null != onNumChangeListener) {
                onNumChangeListener.onNumChanged(mCount);
            }
        } else {
            Toast.makeText(getContext(), "购车车商品数量为0", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当 + 事件触发 回调
     */
    @Override
    protected void onAddClick() {
        if (mCount < mMaxCount) {
            mCount++;
            onAddSuccessListener();
            invalidate();
            if (null != onNumChangeListener) {
                onNumChangeListener.onNumChanged(mCount);
            }
        } else {
            Toast.makeText(getContext(), "不能继续加了！", Toast.LENGTH_SHORT).show();
        }
    }

    OnNumChangeListener onNumChangeListener;

    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }

    public interface OnNumChangeListener {
        void onNumChanged(int count);
    }


}
