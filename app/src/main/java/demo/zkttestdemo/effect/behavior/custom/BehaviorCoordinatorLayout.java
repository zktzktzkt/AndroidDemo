package demo.zkttestdemo.effect.behavior.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import demo.zkttestdemo.R;

/**
 * Created by zkt on 19/02/11.
 * Description:
 */
public class BehaviorCoordinatorLayout extends RelativeLayout {
    public BehaviorCoordinatorLayout(Context context) {
        super(context);
    }

    public BehaviorCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BehaviorCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    class LayoutParams extends RelativeLayout.LayoutParams {

        private Behavior behavior;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.BehaviorCoordinatorLayout);
            String clazzName = array.getString(R.styleable.BehaviorCoordinatorLayout_layout_behavior);
            behavior = parseBehavior(c, attrs, clazzName);
            array.recycle();
        }

        private Behavior parseBehavior(Context context, AttributeSet attrs, String name) {
            if (TextUtils.isEmpty(name)) {
                return null;
            }

            try {
                Class<?> aClass = Class.forName(name);
                Constructor<?> c = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
                c.setAccessible(true);
                return (Behavior) c.newInstance(context, attrs);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(RelativeLayout.LayoutParams source) {
            super(source);
        }
    }
}
