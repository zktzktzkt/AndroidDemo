package demo.zkttestdemo.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by zkt on 18/12/07.
 * Description:
 */
public class MaterialEditText extends AppCompatEditText {

    public static final float TEXT_SIZE = dp2px(12);
    public static final float TEXT_MARGIN = dp2px(8);
    public static final int TEXT_VERTICAL_OFFSET = dp2px(22);
    public static final int TEXT_HORIZONTAL_OFFSET = dp2px(5);
    public static final int TEXT_ANIMATION_OFFSET = dp2px(16);
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    boolean floatingLabelShown;
    float floatingLabelFraction;
    private ObjectAnimator animator;
    private ObjectAnimator animatorReverse;

    public float getFloatingLabelFraction() {
        return floatingLabelFraction;
    }

    public void setFloatingLabelFraction(float floatingLabelFraction) {
        this.floatingLabelFraction = floatingLabelFraction;
        invalidate();
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setPadding(getPaddingLeft(), (int) (getPaddingTop() + TEXT_SIZE + TEXT_MARGIN), getPaddingRight(), getPaddingBottom());
        paint.setTextSize(TEXT_SIZE);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (floatingLabelShown && TextUtils.isEmpty(getText())) {
                    //消失
                    floatingLabelShown = false;
                    getAnimatorReverse().start();
                } else if (!floatingLabelShown && !TextUtils.isEmpty(getText())) {
                    //出现
                    floatingLabelShown = true;
                    getAnimator().start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ObjectAnimator getAnimator() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelFraction", 1);
        }
        return animator;
    }

    private ObjectAnimator getAnimatorReverse() {
        if (animatorReverse == null) {
            animatorReverse = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelFraction", 0);
        }
        return animatorReverse;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha((int) (0xff * floatingLabelFraction));
        float extraOffset = TEXT_ANIMATION_OFFSET * (1 - floatingLabelFraction);
        canvas.drawText(getHint().toString(), TEXT_HORIZONTAL_OFFSET, TEXT_VERTICAL_OFFSET + extraOffset, paint);
    }

    private static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
