package demo.zkttestdemo.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zkt on 18/12/04.
 * Description:
 */
public class ClickUtil {
    private static Method sHookMethod;
    private static Field sHookField;
    private static int mPrivateTagKey;
    private static IProxyClickListener mInnerClickProxy;

    public static void init(IProxyClickListener innerClickProxy) {
        mInnerClickProxy = innerClickProxy;
        if (sHookMethod == null) {
            try {
                Class viewClass = Class.forName("android.view.View");
                if (viewClass != null) {
                    sHookMethod = viewClass.getDeclaredMethod("getListenerInfo");
                    if (sHookMethod != null) {
                        sHookMethod.setAccessible(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sHookField == null) {
            try {
                Class listenerInfoClass = Class.forName("android.view.View$ListenerInfo");
                if (listenerInfoClass != null) {
                    sHookField = listenerInfoClass.getDeclaredField("mOnClickListener");
                    if (sHookField != null) {
                        sHookField.setAccessible(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void hookViews(View view, int recycledContainerDeep) {
        if (view.getVisibility() == View.VISIBLE) {
            boolean forceHook = recycledContainerDeep == 1;
            if (view instanceof ViewGroup) {
                boolean existAncestorRecycle = recycledContainerDeep > 0;
                ViewGroup p = (ViewGroup) view;
                if (!(p instanceof AbsListView || p instanceof RecyclerView) || existAncestorRecycle) {
                    hookClickListener(view, recycledContainerDeep, forceHook);
                    if (existAncestorRecycle) {
                        recycledContainerDeep++;
                    }
                } else {
                    recycledContainerDeep = 1;
                }
                int childCount = p.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = p.getChildAt(i);
                    hookViews(child, recycledContainerDeep);
                }
            } else {
                hookClickListener(view, recycledContainerDeep, forceHook);
            }
        }
    }

    private static void hookClickListener(View view, int recycledContainerDeep, boolean forceHook) {
        boolean needHook = forceHook;
        if (!needHook) {
            needHook = view.isClickable();
            if (needHook && recycledContainerDeep == 0) {
                needHook = view.getTag(mPrivateTagKey) == null;
            }
        }
        if (needHook) {
            try {
                Object getListenerInfo = sHookMethod.invoke(view);
                View.OnClickListener baseClickListener = getListenerInfo == null ? null : (View.OnClickListener) sHookField.get(getListenerInfo);//获取已设置过的监听器
                if ((baseClickListener != null && !(baseClickListener instanceof IProxyClickListener.WrapClickListener))) {
                    sHookField.set(getListenerInfo, new IProxyClickListener.WrapClickListener(baseClickListener, mInnerClickProxy));
                    view.setTag(mPrivateTagKey, recycledContainerDeep);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface IProxyClickListener {

        boolean onProxyClick(WrapClickListener wrap, View v);

        class WrapClickListener implements View.OnClickListener {
            IProxyClickListener mProxyListener;
            View.OnClickListener mBaseListener;

            public WrapClickListener(View.OnClickListener l, IProxyClickListener proxyListener) {
                mBaseListener = l;
                mProxyListener = proxyListener;
            }

            @Override
            public void onClick(View v) {
                boolean handled = mProxyListener == null ? false : mProxyListener.onProxyClick(WrapClickListener.this, v);
                if (!handled && mBaseListener != null) {
                    mBaseListener.onClick(v);
                }
            }
        }
    }
}
