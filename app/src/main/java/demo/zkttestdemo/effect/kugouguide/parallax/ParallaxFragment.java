package demo.zkttestdemo.effect.kugouguide.parallax;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import demo.zkttestdemo.R;

/**
 * 关键点：获取xml中的自定义属性，并设置给View
 */
public class ParallaxFragment extends Fragment implements LayoutInflater.Factory2 {
    public static final String LAYOUT_ID_KEY = "LAYOUT_ID_KEY";
    private CompatViewInflater mCompatViewInflater;
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;

    //存放所有需要位移的View
    private List<View> mParallaxViews = new ArrayList<>();

    private int[] mParallaxAttrs = new int[]{R.attr.translationXIn,
            R.attr.translationXOut, R.attr.translationYIn, R.attr.translationYOut};

    /**
     * @param inflater 这个是Activity的inflater，inflater.inflate的时候，调用的onCreateView是Activity中的onCreateView方法
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 获取布局的Id
        int layoutId = getArguments().getInt(LAYOUT_ID_KEY);

        // 0. “多态”的概念
        // 1. AppCompatActivity 中实现了LayoutInflater.Factory2 接口
        // 2. 所以inflater.inflate()的时候，会回调的是AppCompatActivity的onCreateView
        // 3. 我想在Fragment中单独处理创建View的逻辑，所以AppCompatActivity中那一套创建View的逻辑，可以直接复制粘贴过来
        // 4. 为什么需要自己实现创建View的逻辑？因为我要自己解析View的属性。

        inflater = inflater.cloneInContext(getActivity());
        LayoutInflaterCompat.setFactory2(inflater, this);

        // 先判断Factory2是否为null；如果不为null，就会调用其中的onCreateView；如果onCreateView的return为null，就会调用自己的一套逻辑
        return inflater.inflate(layoutId, container, false);
    }

    /**
     * 自己创建View，自己解析属性
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //1. 创建View
        View view = createView(parent, name, context, attrs);

        if (view != null) {
            Log.e("TAG", view.toString());
            //解析所有的我们自己关注的属性
            analysisAttr(view, context, attrs);
        }
        return view;
    }

    private void analysisAttr(View view, Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, mParallaxAttrs);

        if (typedArray != null && typedArray.getIndexCount() != 0) {
            ParallaxTag tag = new ParallaxTag();
            tag.translationXIn = typedArray.getFloat(0, 0f);
            tag.translationXOut = typedArray.getFloat(1, 0f);
            tag.translationYIn = typedArray.getFloat(2, 0f);
            tag.translationYOut = typedArray.getFloat(3, 0f);
            view.setTag(R.id.parallax_tag, tag);

            Log.e("TAG", tag.toString());
            mParallaxViews.add(view);
        }

        typedArray.recycle();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @SuppressLint("RestrictedApi")
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mCompatViewInflater == null) {
            mCompatViewInflater = new CompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }


    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View) || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public List<View> getParallaxViews() {
        return mParallaxViews;
    }
}
