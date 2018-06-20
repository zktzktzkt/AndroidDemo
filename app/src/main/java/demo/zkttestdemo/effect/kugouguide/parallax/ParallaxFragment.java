package demo.zkttestdemo.effect.kugouguide.parallax;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {
    public static final String LAYOUT_ID_KEY = "LAYOUT_ID_KEY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取布局的Id
        int layoutId = getArguments().getInt(LAYOUT_ID_KEY);
        // view创建的时候 解析属性
        LayoutInflaterCompat.setFactory(inflater, this);

        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // View都会来这里
        return null;
    }
}
