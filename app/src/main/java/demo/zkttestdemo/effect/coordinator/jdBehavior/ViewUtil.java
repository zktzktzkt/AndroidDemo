package demo.zkttestdemo.effect.coordinator.jdBehavior;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jd.lib.search.core.R;
import com.jd.lib.search.core.constants.Constant;
import com.jingdong.app.mall.bundle.mobileConfig.JDMobileConfig;
import com.jingdong.common.unification.uniconfig.UnIconConfigHelper;
import com.jingdong.common.utils.DeepDarkChangeManager;
import com.jingdong.common.utils.DeepDarkUtils;
import com.jingdong.common.utils.text.ScaleModeConstants;
import com.jingdong.common.utils.text.TextScaleModeHelper;
import com.jingdong.jdsdk.JdSdk;
import com.jingdong.sdk.eldermode.util.JDElderModeUtils;

import demo.zkttestdemo.DataUtil;

/**
 * 【View相关属性设置以及获取工具类】
 * @author mengfanlei
 * @date 2018/12/26
 */
public class ViewUtil {

    /**
     * 防止快速重复点击
     */
    private static long lastClickTime;

    /**
     * 快速重复点击，默认500
     * @return 是否快速点击
     */
    public synchronized static boolean isFastClick() {
        return isFastClick(Constant.COMMON_DELTA_TIME);
    }

    /**
     * 快速重复点击，(可自定义时长阈值)
     * @param deltaTime 重复点击时长的阈值
     */
    public synchronized static boolean isFastClick(long deltaTime) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < deltaTime && time - lastClickTime > 0) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 快速重复点击，1500
     *
     * @return 是否快速点击
     */
    public synchronized static boolean isFastClickLong() {
        return isFastClick(Constant.LONG_DELTA_TIME);
    }

    /**
     * 设置单个View VISIBLE
     */
    public static void visible(View view) {
        if (view instanceof ViewStub) {
            view.setVisibility(View.VISIBLE);
            return;
        }
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置多个View VISIBLE
     */
    public static void visible(View... views) {
        for (View view : views) {
            visible(view);
        }
    }

    /**
     * 设置单个View INVISIBLE
     */
    public static void invisible(View view) {
        if (null == view) {
            return;
        }
        if (view instanceof ViewStub) {
            view.setVisibility(View.INVISIBLE);
            return;
        }
        if (view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置多个View INVISIBLE
     */
    public static void invisible(View... views) {
        for (View view : views) {
            invisible(view);
        }
    }

    /**
     * 设置单个View GONE
     */
    public static void gone(View view) {
        if (null == view) {
            return;
        }
        if (view instanceof ViewStub) {
            view.setVisibility(View.GONE);
            return;
        }
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置多个View GONE
     */
    public static void gone(View... views) {
        for (View view : views) {
            gone(view);
        }
    }

    /**
     * 设置View VISIBLE OR INVISIBLE
     */
    public static void visibleOrInvisible(View view, boolean visible) {
        if (view != null) {
            if (visible) {
                visible(view);
            } else {
                invisible(view);
            }
        }
    }

    /**
     * 设置View VISIBLE OR GONE
     */
    public static void visibleOrGone(View view, boolean visible) {
        if (view != null) {
            if (visible) {
                visible(view);
            } else {
                gone(view);
            }
        }
    }

    /**
     * 判断View当前是否 VISIBLE
     */
    public static boolean isVisible(View view) {
        if (view instanceof ViewStub) {
            throw new RuntimeException("查看ViewStub的状态 请使用mInflatedId对应的view或其他方式检查");
        }
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    /**
     * 判断View当前是否 INVISIBLE
     */
    public static boolean isInvisible(View view) {
        if (view instanceof ViewStub) {
            throw new RuntimeException("查看ViewStub的状态 请使用mInflatedId对应的view或其他方式检查");
        }
        return view != null && view.getVisibility() == View.INVISIBLE;
    }

    /**
     * 判断View当前是否 GONE
     */
    public static boolean isGone(View view) {
        if (view instanceof ViewStub) {
            throw new RuntimeException("查看ViewStub的状态 请使用mInflatedId对应的view或其他方式检查");
        }
        return view != null && view.getVisibility() == View.GONE;
    }

    /**
     * 获取View的宽度
     */
    public static int getViewWidth(View view) {
        if (view != null) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            return view.getMeasuredWidth();
        }
        return 0;
    }

    /**
     * 获取View的高度
     */
    public static int getViewHeight(View view) {
        if (view != null) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            return view.getMeasuredHeight();
        }
        return 0;
    }
    /**
     * 获取View的高度
     */
    public static int getViewHeightVisible(View view) {
        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            return view.getMeasuredHeight();
        }
        return 0;
    }

    /**
     * 设置View的最小宽度
     */
    public static void setMinWidth(View view, int minWidth) {
        if (view != null) {
            view.setMinimumHeight(minWidth);
        }
    }

    /**
     * 设置View的最小高度
     */
    public static void setMinHeight(View view, int minHeight) {
        if (view != null) {
            view.setMinimumHeight(minHeight);
        }
    }

    /**
     * 设置View的padding
     */
    public static void setViewPadding(View view, int left, int top, int right, int bottom) {
        if (view != null) {
            view.setPadding(left, top, right, bottom);
        }
    }
    /**
     * 向容器添加子View
     */
    public static void addView(ViewGroup viewGroup, View childView) {
        if (viewGroup != null && childView != null) {
            viewGroup.addView(childView);
        }
    }

    /**
     * 移除子View
     */
    public static void removeChildView(ViewGroup viewGroup, View view) {
        if (viewGroup != null && view != null) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                if (viewGroup.getChildAt(i) == view) {
                    viewGroup.removeView(view);
                    break;
                }
            }
        }
    }

    /**
     * 移除子View
     */
    public static void removeAllViews(ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
    }

    /**
     * 隐藏ViewGroup中的子View
     */
    public static void hideChildViews(ViewGroup viewGroup) {
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ViewUtil.gone(viewGroup.getChildAt(i));
            }
        }
    }

    /**
     * 设置单个View透明度
     */
    public static void setViewAlpha(View view, float alpha) {
        if (view != null) {
            view.setAlpha(alpha);
        }
    }

    /**
     * 设置多个View透明度
     */
    public static void setViewAlpha(float alpha, View... views) {
        for (View view : views) {
            setViewAlpha(view, alpha);
        }
    }

    /**
     * 设置View背景
     * @param drawableId 直接传R.drawable.xxx即可
     */
    public static void setViewBackground(View view, int drawableId) {
        if (view != null) {
            ViewCompat.setBackground(view, ResourceUtil.getDrawable(drawableId));
        }
    }

    /**
     * 设置View背景
     * @param drawable
     */
    public static void setViewBackgroundDrawable(View view, Drawable drawable) {
        if (view != null) {
            ViewCompat.setBackground(view, drawable);
        }
    }

    /**
     * 设置View背景
     * @param drawable drawable
     */
    public static void setViewBackground(View view, Drawable drawable) {
        if (view != null) {
            ViewCompat.setBackground(view, drawable);
        }
    }

    /**
     * 设置View背景
     * @param drawableId drawableId
     */
    public static void setViewBackgroundResource(View view, int drawableId) {
        if (view != null) {
            view.setBackgroundResource(drawableId);
        }
    }

    /**
     * 设置View背景颜色
     * @param colorId 直接传R.color.xxx即可
     */
    public static void setViewBackgroundColor(View view, int colorId) {
        if (view != null) {
            view.setBackgroundColor(ResourceUtil.getColorId(colorId));
        }
    }

    /**
     * 设置View背景颜色
     * @param colorId 直接传colorId
     */
    public static void setViewBackgroundColorWithColor(View view, int colorId) {
        if (view != null) {
            view.setBackgroundColor(colorId);
        }
    }

    /**
     * 设置View背景颜色,支持暗黑+长辈版（BR）
     * 主站产品色
     * F5F5F5–141212
     */
    public static void setViewBackgroundColorWithBR(View view) {
        if (view != null) {
            view.setBackgroundColor(JDElderModeUtils.getColorBrBySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }

    /**
     * 设置View背景颜色,支持暗黑+长辈版（BG1）
     * F5F5F5–141212
     */
    public static void setViewBackgroundColorWithBg1(View view) {
        if (view != null) {
            view.setBackgroundColor(JDElderModeUtils.getColorBg1BySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }

    /**
     * 设置View背景颜色,支持暗黑+长辈版（BG2）
     * FFFFFF—1D1B1B
     */
    public static void setViewBackgroundColorWithBg2(View view) {
        if (view != null) {
            view.setBackgroundColor(JDElderModeUtils.getColorBg2BySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }

    /**
     * 设置View背景颜色,支持暗黑+长辈版（BG3）
     * F5F5F5—302E2E
     */
    public static void setViewBackgroundColorWithBg3(View view) {
        if (view != null) {
            view.setBackgroundColor(JDElderModeUtils.getColorBg3BySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }

    /**
     * 设置View无背景
     * @param view
     */
    public static void setViewNullBackground(View view) {
        if (view != null) {
            ViewCompat.setBackground(view, null);
        }
    }

    /**
     * 设置显示图片，并需要Default资源
     * **/
    public static void showIconResCodeTagWithDefault(ImageView imageView, int defaultDrawable, String adIconResCode) {
        if (imageView == null || TextUtils.isEmpty(adIconResCode)) {
            return;
        }
        Bitmap bitmap = UnIconConfigHelper.getBitmap(adIconResCode);
        if (bitmap != null) {
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageBitmap(null);
            imageView.setImageDrawable(ResourceUtil.getDrawable(defaultDrawable));
        }
    }

    /**
     * 设置显示图片，并需要Default资源
     * **/
    public static void showIconResCodeTagWithDefault(ImageView imageView, Drawable defaultDrawable, String adIconResCode) {
        if (imageView == null) {
            return;
        }
        Bitmap bitmap = UnIconConfigHelper.getBitmap(adIconResCode);
        if (bitmap != null) {
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageBitmap(null);
            imageView.setImageDrawable(defaultDrawable);
        }
    }
    /**
     * 设置View的可用态
     */
    public static void setViewEnabled(View view, boolean enable) {
        if (view != null) {
            view.setEnabled(enable);
        }
    }

    /**
     * 设置View的选中态
     */
    public static void setViewSelected(View view, boolean select) {
        if (view != null) {
            view.setSelected(select);
        }
    }

    /**
     * 获取View的选中态
     */
    public static boolean isSelected(View view) {
        return view != null && view.isSelected();
    }

    /**
     * 设置View的check状态
     */
    public static void setViewChecked(CompoundButton compoundBtn, boolean check) {
        if (compoundBtn != null) {
            compoundBtn.setChecked(check);
        }
    }

    /**
     * 设置View的clickable状态
     */
    public static void setViewClickable(View view, boolean clickable) {
        if (view != null) {
            view.setClickable(clickable);
        }
    }

    /**
     * 向上旋转动画
     */
    public static void upArrow(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", -180f, 0f);
        objectAnimator.setDuration(0);
        objectAnimator.start();
    }

    /**
     * 向下旋转动画
     */
    public static void downArrow(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f);
        objectAnimator.setDuration(0);
        objectAnimator.start();
    }

    /**
     * TextView setText
     */
    public static void setText(TextView textView, String text) {
        if (textView != null) {
            textView.setText(text);
        }
    }
    /**
     * TextView setText
     */
    public static void setTextUnderline(TextView textView, String text) {
        if (textView != null&&!TextUtils.isEmpty(text)) {
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
    }

    /**
     * TextView setText
     * 此方法stringId，一定要保证是正确的字符串资源id，否则会抛异常，不加try catch目的是为了让异常能在开发过程中被发现
     */
    public static void setText(TextView textView, int stingId) {
        if (textView != null) {
            textView.setText(stingId);
        }
    }

    /**
     * TextView setText
     */
    public static void setText(TextView textView, CharSequence text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    /**
     * TextView setTextSize
     */
    public static void setTextSize(TextView textView, float textSize) {
        if (textView != null) {
            textView.setTextSize(textSize);
        }
    }

    /**
     * TextView setTextSize
     * @param unit 单位
     */
    public static void setTextSize(TextView textView, int unit, float textSize) {
        if (textView != null) {
            textView.setTextSize(unit, textSize);
        }
    }

    /**
     * TextView setTextSize
     * 字体适配：支持大小号+长辈版
     */
    public static void setTextSizeWithAdapterModeSp(TextView textView, float textSize) {
        if (textView != null) {
            if (JDElderModeUtils.isElderMode()){
                textView.setTextSize(JDElderModeUtils.getElderTextSize(textSize));
            }else{
                textView.setTextSize(TextScaleModeHelper.Companion.getInstance().getScaleSize(JdSdk.getInstance().getApplicationContext(), textSize));
            }
        }
    }

    /**
     * TextView setTextSize
     * @param textSize 大小 sp
     */
    public static void setTextSizeWithScaleModeSp(TextView textView, float textSize) {
        if (textView != null) {
            textView.setTextSize(TextScaleModeHelper.Companion.getInstance().getScaleSize(JdSdk.getInstance().getApplicationContext(), textSize));
        }
    }

    /**
     * 获取字体大小
     */
    public static float getTextSizeWithAdapterModeSp(float textSize){
        if (JDElderModeUtils.isElderMode()){
            return JDElderModeUtils.getElderTextSize(textSize);
        }else{
            return TextScaleModeHelper.Companion.getInstance().getScaleSize(JdSdk.getInstance().getApplicationContext(), textSize);
        }
    }

    /**
     * 图标适配老年版 长辈版
     * @param imageView
     * @param defaultSize
     * @param elderSize
     */
    public static void setImageSizeWWithScaleModeDp(ImageView imageView,float defaultSize,float elderSize){
        if (imageView!=null&&imageView.getLayoutParams()!=null){
            int size = DisplayUtil.dp2px((ViewUtil.isElderMode()|| isLargeScaleMode())
                    ?elderSize:defaultSize);
            imageView.getLayoutParams().width = size;
            imageView.getLayoutParams().height = size;
        }
    }
    /**
     * 是否是 大字体 模式
     */
    public static boolean isLargeScaleMode() {
        return !TextUtils.equals(TextScaleModeHelper.Companion.getInstance().getTextSizeScaleMode(), ScaleModeConstants.TEXT_SCALE_MODE_STANDARD);
    }

    /**
     * TextView setTextColor
     * @param colorId 直接传R.color.xxx即可
     */
    public static void setTextColor(TextView textView, int colorId) {
        if (textView != null) {
            textView.setTextColor(ResourceUtil.getColorId(colorId));
        }
    }

    /**
     * TextView setTextColor
     * @param colorId 传R.color对应的getResources获取的id值
     */
    public static void setTextColorInt(TextView textView, int colorId) {
        if (textView != null) {
            textView.setTextColor(colorId);
        }
    }
    /**
     * TextView setTextColor
     * @param colorId 直接传R.color.xxx即可
     */
    public static void setTextColorStateList(TextView textView, int colorId) {
        if (textView != null) {
            ColorStateList stateList = ResourceUtil.getColorStateList(colorId);
            if (stateList != null) {
                textView.setTextColor(stateList);
            }
        }
    }

    /**
     * TextView setTextColor
     * @param colorId
     */
    public static void setTextColorWithColor(TextView textView, int colorId) {
        if (textView != null) {
            textView.setTextColor(colorId);
        }
    }

    /**
     * 设置TextView提示文案颜色
     */
    public static void setHintTextColor(TextView textView, int colorId) {
        if (textView != null) {
            textView.setHintTextColor(ResourceUtil.getColorId(colorId));
        }
    }

    /**
     * TextView setTextColor
     * 此方法colorString，一定要保证是正确的颜色字符串(例如：#ff00ff或#00ff00ff)，否则会抛异常，不加try catch目的是为了让异常能在开发过程中被发现
     */
    public static void setTextColor(TextView textView, String colorString) {
        if (textView != null && !TextUtils.isEmpty(colorString)) {
            textView.setTextColor(Color.parseColor(colorString));
        }
    }

    /**
     * 设置多个TextView的颜色
     */
    public static void setTextColor(int colorId, TextView... textViews) {
        for (TextView textView : textViews) {
            setTextColor(textView, colorId);
        }
    }

    /**
     * 设置TextView的颜色,支持暗黑+长辈版(BR)
     * 主站产品色
     * FA2C19—FF3826
     */
    public static void setTextColorWithBR(TextView textView) {
        if (textView != null){
            textView.setTextColor(JDElderModeUtils.getColorBrBySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }

    /**
     * 设置TextView的颜色,支持暗黑+长辈版(C1)
     * 1A1A1A—ECECEC
     */
    public static void setTextColorWithC1(TextView textView) {
        if (textView != null){
            textView.setTextColor(JDElderModeUtils.getColorC1BySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }

    /**
     * 设置TextView的颜色,支持暗黑+长辈版(C2)
     * 808080—848383
     */
    public static void setTextColorWithC2(TextView textView) {
        if (textView != null){
            textView.setTextColor(JDElderModeUtils.getColorC2BySupportMode(JDElderModeUtils.SUPPORT_MODE_ELDER_MODE | JDElderModeUtils.SUPPORT_MODE_DARK_MODE));
        }
    }



    /**
     * 设置View的宽度
     */
    public static void setTextWidth(TextView textView, int width) {
        if (textView != null) {
            textView.setWidth(width);
        }
    }

    /**
     * 设置View最大宽度
     */
    public static void setTextMaxWidth(TextView textView, int maxWidth) {
        if (textView != null) {
            textView.setMaxWidth(maxWidth);
        }
    }

    /**
     * 设置View最大行数
     */
    public static void setTextMaxLine(TextView textView, int maxLine) {
        if (textView != null) {
            textView.setMaxLines(maxLine);
        }
    }

    /**
     * TextView setTextTypeFace
     * @param typeface Typeface.BOLD；Typeface.DEFAULT
     */
    public static void setTextTypeFace(TextView textView, int typeface) {
        if (textView != null) {
            textView.setTypeface(Typeface.defaultFromStyle(typeface));
        }
    }

    /**
     * 设置TextView在结尾处显示省略号
     */
    public static void setTextEllipsizeEnd(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    /**
     * TextView底部设置图片
     */
    public static void setTextDrawablePadding(TextView textView, int padding) {
        if (textView != null) {
            textView.setCompoundDrawablePadding(padding);
        }
    }

    /**
     * TextView右侧设置图片
     */
    public static void setTextRightDrawable(TextView textView, int drawableId) {
        if (textView != null) {
            Drawable drawable = ResourceUtil.getDrawable(drawableId);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, null, drawable, null);
            }
        }
    }

    /**
     * TextView设置右图片
     * @param textView
     * @param drawable
     */
    public static void setTextRightDrawable(TextView textView, Drawable drawable){
        if (textView != null && drawable != null){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    /**
     * TextView底部设置图片
     */
    public static void setTextBottomDrawable(TextView textView, int drawableId) {
        if (textView != null) {
            Drawable drawable = ResourceUtil.getDrawable(drawableId);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(null, null, null, drawable);
            }
        }
    }

    /**
     * TextView去除drawable
     */
    public static void setTextNullDrawable(TextView textView) {
        if (textView != null) {
            textView.setCompoundDrawables(null, null, null, null);
        }
    }

    /**
     * 设置图片资源
     * @param resourceId 图片资源id 即R.drawable.xxx
     */
    public static void setImageResource(ImageView imageView, int resourceId) {
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
    }

    /**
     * 设置图片资源
     * @param drawableId drawable资源 即R.drawable.xxx
     */
    public static void setImageDrawable(ImageView imageView, int drawableId) {
        if (imageView != null) {
            imageView.setImageDrawable(ResourceUtil.getDrawable(drawableId));
        }
    }

    /**
     * 设置图片资源
     * @param imageView
     * @param drawable
     */
    public static void setImageDrawable(ImageView imageView, Drawable drawable){
        if (imageView != null && drawable != null){
            imageView.setImageDrawable(drawable);
        }
    }

    /**
     * 设置图片资源
     * @param
     * @param drawable
     */
    public static void setDrawableColor(Drawable drawable,@ColorInt int color){
        if (drawable != null){
            drawable.setColorFilter(new PorterDuffColorFilter(color,
                    PorterDuff.Mode.SRC_ATOP) );
        }
    }

    /**
     * 设置图片Bitmap
     */
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置图片圆角
     */
    public static void setSimpleImageCorner(Context context, SimpleDraweeView view, float radius) {
        if (view != null && context != null) {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);
            GenericDraweeHierarchyBuilder builder = GenericDraweeHierarchyBuilder.newInstance(context.getResources());
            builder.setRoundingParams(roundingParams);
            view.setHierarchy(builder.build());
        }
    }

    /**根据字数限制要求，截断字符串并添加省略号
     * @param text 原文本
     * @param length 限制长度
     * @param isAddEllipsis 是否加省略号
     * @return 截断后字符串
     */
    public static String cutString(String text, int length, boolean isAddEllipsis){
        if(TextUtils.isEmpty(text)){
            return "";
        }
        if(text.length()>length){
            StringBuilder stringBuilder = new StringBuilder(text.substring(0,length));
            if(isAddEllipsis){
                stringBuilder.append("...");
            }
            return stringBuilder.toString();
        }else {
            return text;
        }
    }

    /**
     * 设置textview的左边drawable
     * @param textView
     * @param drawableId
     */
    public static void setTextLeftDrawable(TextView textView, int drawableId) {
        if (textView != null) {
            Drawable drawable = ResourceUtil.getDrawable(drawableId);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    /**
     * TextView设置左图片
     * @param textView
     * @param drawable
     */
    public static void setTextLeftDrawable(TextView textView, Drawable drawable) {
        if (textView != null && drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    /** 对TextView设置不同状态时其文字颜色。 */
    public static ColorStateList createColorStateList(int pressed, int selected, int enabled,int normal,int unable) {
        int[] colors = new int[] { pressed,selected,  enabled, normal, unable };
        int[][] states = new int[5][];
        states[0] = new int[] { android.R.attr.state_pressed};
        states[1] = new int[] { android.R.attr.state_selected};
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { -android.R.attr.state_selected};
        states[4] = new int[] { -android.R.attr.state_enabled};

        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 是否暗黑模式
     * @return true暗黑模式 false不是暗黑模式
     */
    public static boolean isDarkMode() {
        return DeepDarkChangeManager.getInstance().getUIMode() == DeepDarkChangeManager.MODE_DARK;
    }


    /**
     * 是否是长辈版
     * @return true长辈版 false老年版
     */
    public static boolean isElderMode() {
        return JDElderModeUtils.isElderMode();
    }

    /**
     * 设置TextView文本颜色
     * @param textView
     * @param colorString
     */
    public static void setTextDarkColor(TextView textView, String colorString){
        if (textView != null){
            textView.setTextColor(DeepDarkUtils.getInstance().getDarkColorFromJson(colorString));
        }
    }

    /**
     * 设置View暗黑背景颜色
     * @param colorString 直接传R.color.xxx即可
     */
    public static void setViewBackgroundColor(View view, String colorString) {
        if (view != null) {
            view.setBackgroundColor(DeepDarkUtils.getInstance().getDarkColorFromJson(colorString));
        }
    }

    /**
     * 改变图标的颜色
     * @param imageView
     * @param color
     */
    public static void setColorFilter(ImageView imageView, int color){
        if (imageView != null){
            imageView.setColorFilter(ResourceUtil.getColorId(color));
        }
    }

    /**
     * 改变图标的颜色
     * @param imageView
     * @param color
     */
    public static void setColorFilterColorInt(ImageView imageView, @ColorInt  int color){
        if (imageView != null){
            imageView.setColorFilter(color);
        }
    }

    /**
     * 搜索结果页右下角图标涂色、设置背景
     * @param imageView
     */
    public static void setBottomRightColorFilter(ImageView imageView,Drawable drawable){
        if (imageView != null){
            int color = isDarkMode()? R.color.lib_search_color_E8E8E8
                    :R.color.lib_search_color_252525;
            imageView.setColorFilter(ResourceUtil.getColorId(color));
            imageView.setBackground(drawable);
        }
    }


    /**
     * 改变图标的颜色
     * @param imageView
     */
    public static void clearColorFilter(ImageView imageView){
        if (imageView != null){
            imageView.clearColorFilter();
        }
    }

    /**
     * 创建暗模式Drawable
     * @param drawable
     */
    public static Drawable createDarkModeDrawable(Drawable drawable, int color) {
        if (drawable != null){
            Drawable darkDrawable = drawable;
            DrawableCompat.setTint(darkDrawable, ResourceUtil.getColorId(color));
            return darkDrawable;
        }
        return null;
    }

    /**
     * 是否异步加载布局
     * @return true 是
     */
    public static boolean isAsyncLayout() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "searchAsyncLayoutSwitcher", "searchAsyncLayoutSwitcher", "0"), "1");
    }
    /**
     * 多广告店铺开关
     * @return true 是
     */
    public static boolean isMultiAdsShopOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "search936AdShopPopShopExpoSwitcher", "search936AdShopPopShopExpoSwitcher", "0"), "1");
    }

    /**
     * 判断是否显示更多热榜
     * 0 显示
     * 1 不显示
     */
    public static boolean isShowMoreHotRank() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "hotSearchListMore", "hideMoreBtn", "0"), "0");
    }

    /**
     * 获取搜索结果页卡片播放动画开关是否打开
     * @return true：打开 false关闭
     */
    public static boolean isItemAnimatorSwitchOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "searchItemAnimatorSwitcher", "searchItemAnimatorSwitcher", "0"), "1");
    }

    /**
     * 获取搜索页语音搜索的新样式开关是否打开
     * @return true：打开 false关闭
     */
    public static boolean isVoiceSearchSwitcherOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "voiceInput", "isNewStyle", "0"), "1");
    }

    /**
     * 获取屏蔽功能开关是否打开
     * @return true：打开 false关闭
     */
    public static boolean isTouchEnableSwitcherOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "touchEnable", "touchEnable", "0"), "1");
    }
    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 注意两个开关不能同时为true注意两个开关不能同时为true注意两个开关不能同时为true  否则第二个开关下的代码不生效
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 获取删除recyclerview中item 相关代码的开关是否打开  注意两个开关不能同时为true
     * @return true：打开 false关闭
     */
    public static boolean isRecyclerDeleteSwitcherOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "recyclerViewDelete", "switcher", "0"), "1");
    }
    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 注意两个开关不能同时为true注意两个开关不能同时为true注意两个开关不能同时为true  否则第二个开关下的代码不生效
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 获取删除recyclerview中item 相关代码的开关是否打开
     * @return true：打开 false关闭
     */
    public static boolean isRecyclerDeleteSwitcherNotityOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "recyclerViewDeleteNotity", "switcherNotify", "0"), "1");
    }
    /**
     * 获取配置平台开关
     * @param space 空间名
     * @param setupKey 配置key
     * @param switcherKey 全量字段
     */
    public static boolean getMobileConfig(String space, String setupKey,String switcherKey){
        if (TextUtils.isEmpty(space) || TextUtils.isEmpty(setupKey) || TextUtils.isEmpty(switcherKey)){
            return false;
        }
        return TextUtils.equals(Constant.ONE_STRING,JDMobileConfig.getInstance().getConfig(space,setupKey,switcherKey,"0"));
    }

    /**
     * View生成Bitmap
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromViewBySystem(View v) {
        if (v == null) {
            return null;
        }
        String blurValue = JDMobileConfig.getInstance().getConfig("JDSearch","addBlurBg","blur");
        if (!TextUtils.equals(Constant.ZERO_STRING,blurValue)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                //测量使得view指定大小
                Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bmp);
                c.drawColor(Color.WHITE);
                v.draw(c);
                return bmp;
            }else{
                return loadBitmapFromViewBySystemOldStyle(v);
            }
        }else{
            return loadBitmapFromViewBySystemOldStyle(v);
        }
    }

    /**
     * View生成Bitmap
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromViewBySystemOldStyle(View v) {
        if (v == null) {
            return null;
        }
        v.destroyDrawingCache();
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }


    /**
     * TextView setTextSize
     * @param textSize 大小 sp
     */
    public static void setTextSizeWithElderModeSp(TextView textView, float textSize) {
        if (textView != null) {
            if (isElderMode()) {
                textView.setTextSize(JDElderModeUtils.getElderTextSize(textSize));
            }else{
                textView.setTextSize(textSize);
            }
        }
    }
    /**
     * TextView setTextSize
     * @param textSize 大小 sp
     */
    public static void setTextSizeWithAnyModeSp(TextView textView, float textSize) {
        if (textView != null) {
            if (isElderMode()) {
                textView.setTextSize(JDElderModeUtils.getElderTextSize(textSize));
            }else{
                ViewUtil.setTextSizeWithScaleModeSp(textView,textSize);
            }
        }
    }

    /**
     * 作用：¥10.00将¥设置成字号fTextsize,将10.00设置成textSize
     *
     * @param price     ¥10.00
     * @param dip       如果dip是true，price传的是dip的值；如果dip是false，参数price 传的是pixel的值
     * @param fTextsize ¥字号
     * @param textSize  价格字号
     * @return
     */
    public static CharSequence getCouponPrice(String price, boolean dip, int fTextsize, int textSize) {
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(price);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(fTextsize, dip), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(textSize, dip), 1, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 作用将100元中"100"设置字号priceTextSize，"元"设置字号yuanTextSize
     *
     * @param price
     * @param dip 如果dip是true，price传的是dip的值；如果dip是false，参数price 传的是pixel的值
     * @param priceTextSize
     * @param yuanTextSize
     * @return
     */
    public static CharSequence getPrice(String price, boolean dip, int priceTextSize, int yuanTextSize) {
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(price);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(priceTextSize, dip), 0, price.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(yuanTextSize, dip), price.length() - 1, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }


    /** 设置商品图片底部状态文本的背景色以及字号大小*/
    public static void setProductStateTagText(TextView textView, String text, int textSize, int bgColorId) {
        ViewUtil.setTextSize(textView, textSize);
        ViewUtil.setText(textView, text);
        if (textView != null) {
            textView.setBackgroundColor(bgColorId);
        }
    }

    /**
     * 设置子价格图标按照比例设置图标宽度(高度12dp）
     * @param priceIconImg
     * @param whRatio
     */
    public static void setSubPriceImgWidth(ImageView priceIconImg, String whRatio) {
        if (priceIconImg == null || TextUtils.isEmpty(whRatio)) {
            return;
        }
        float wtoHRatio = 3.0f;
        try {
            wtoHRatio = Float.parseFloat(whRatio);
        } catch (Exception e) {
        }
        ViewGroup.LayoutParams priceIconImgLp = priceIconImg.getLayoutParams();
        if (priceIconImgLp != null && wtoHRatio != 0) {

            priceIconImgLp.width = (int) (DisplayUtil.dp2px(12) * wtoHRatio);
            priceIconImgLp.height = DisplayUtil.dp2px(12);
            priceIconImg.setLayoutParams(priceIconImgLp);
        }
    }

    /**
     * 渐变色
     * @return
     */
    public static Drawable getOuterLayerDrawable(String[] colors) {
        if (DataUtil.haveNullString(colors)){
            return null;
        }
        int[] tempColors = new int[colors.length];
        int i = 0;
        for (String object : colors) {
            tempColors[i] = ResourceUtil.getColorId(object);
            i++;
        }
        return ViewUtil.getOuterLayerDrawable(tempColors);
    }

    /**
     * 渐变色
     * @return
     */
    public static Drawable getOuterLayerDrawable(int[] colors) {
        if (colors.length == 0){
            return null;
        }
        int startColor = colors[0];
        GradientDrawable linearDrawable = new GradientDrawable();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            linearDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            linearDrawable.setColors(colors);
        } else {
            linearDrawable.setColor(startColor);
        }
        linearDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        linearDrawable.setCornerRadius(DisplayUtil.dp2px(14));
        linearDrawable.setShape(RECTANGLE);
        return linearDrawable;
    }

    /**
     * 获取Drawable
     *
     * @param fillColor    填充颜色
     * @param borderRadius 边框角度
     * @param borderWidth  边框宽度
     * @param borderColor  边框颜色
     * @return
     */
    public static Drawable getDrawable(@ColorInt int fillColor, int borderRadius, float borderWidth, @ColorInt int borderColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fillColor);
        drawable.setCornerRadius(DisplayUtil.dp2px(borderRadius));
        if (borderWidth > 0){
            drawable.setStroke(DisplayUtil.dp2px(borderWidth), borderColor);
        }
        drawable.setShape(RECTANGLE);
        return drawable;
    }
    /**
     * 字符串color转换保护
     * @param color
     * @return
     */
    public static int parseColor(String color){
        int colorInt;
        try {
            colorInt = Color.parseColor(color);
        }catch (Exception e){
            colorInt = Color.parseColor(isDarkMode()?"#ECECEC":"#000000");
        }
        return colorInt;
    }

    /**
     * 门详返回是否需要重刷接口开关
     * true:开关开启-从门详返回需要重刷接口
     * false:开关关闭-从门详返回不需要重刷接口
     */
    public static boolean isNeedRefreshConfigOpen() {
        return TextUtils.equals(JDMobileConfig.getInstance().getConfig("JDSearch", "requestFromHomeDetailSwitcher", "requestFromHomeDetailSwitcher", "0"), "1");
    }

    /**
     * 是否是动态化楼层
     */
    public static boolean isDynamicFloor(String mid){
        return TextUtils.equals(mid,"bpDynamicFloor");
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     * 注意：
     *   1.若View的自定义触摸范围超出Parent的大小，则超出的那部分无效
     *   2.一个Parent只能设置一个View的TouchDelegate，设置多个只有最后设置的生效
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 还原View的触摸和点击响应范围,最小不小于View自身范围
     *
     * @param view
     */
    public static void restoreViewTouchDelegate(final View view) {
        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                bounds.setEmpty();
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * textview是否超出范围显示了省略号
     * @return true 超出了
     */
    public static boolean isOverFlowed(TextView textView) {
        //获取省略的字符数
        if (null == textView || null == textView.getLayout()) {
            return false;
        }
        return textView.getLayout().getEllipsisCount(textView.getLineCount() - 1) != 0;
    }
}
