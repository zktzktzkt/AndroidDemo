package demo.zkttestdemo.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

import demo.zkttestdemo.R;

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
    Rect backgroundPadding = new Rect();

    boolean floatingLabelShown;
    float floatingLabelFraction;
    private ObjectAnimator animator;
    private boolean useFloatingLabel;

    public float getFloatingLabelFraction() {
        return floatingLabelFraction;
    }

    public void setFloatingLabelFraction(float floatingLabelFraction) {
        this.floatingLabelFraction = floatingLabelFraction;
        invalidate();
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //从attrs中取出MaterialEditText数组中对应的元素，比如 new int[]{R.attr.useFloatingLabel}
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
        //MaterialEditText_useFloatingLabel ：只是一个索引，数组的索引，0123。比如typedArray.getBoolean(0)
        useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, true);
        typedArray.recycle();

        if (useFloatingLabel) {
            paint.setTextSize(TEXT_SIZE);
            onUseFloatingLabelChange();
            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (useFloatingLabel) {
                        if (floatingLabelShown && TextUtils.isEmpty(getText())) {
                            //消失
                            floatingLabelShown = false;
                            getAnimator().reverse();
                        } else if (!floatingLabelShown && !TextUtils.isEmpty(getText())) {
                            //出现
                            floatingLabelShown = true;
                            getAnimator().start();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void setUseFloatingLabel(boolean useFloatingLabel) {
        if (this.useFloatingLabel != useFloatingLabel) {
            this.useFloatingLabel = useFloatingLabel;
            onUseFloatingLabelChange();
        }
    }

    private void onUseFloatingLabelChange() {
        getBackground().getPadding(backgroundPadding);
        if (useFloatingLabel) {
            setPadding(getPaddingLeft(), (int) (backgroundPadding.top + TEXT_SIZE + TEXT_MARGIN), getPaddingRight(), getPaddingBottom());
        } else {
            setPadding(getPaddingLeft(), backgroundPadding.top, getPaddingRight(), getPaddingBottom());
        }
    }

    private ObjectAnimator getAnimator() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(MaterialEditText.this, "floatingLabelFraction", 1);
        }
        return animator;
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
