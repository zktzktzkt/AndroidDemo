package com.ypzn.waterbubble;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zkt on 2018-9-10.
 * Description:
 */

public class BubbleView extends View {

    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


}
