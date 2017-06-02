package com.halohoop.collecttaiwan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eaili on 2017/6/2.
 */

public class MapView extends View {

    private List<AreaItem> areaItems;
    private Paint paint;
    private AreaItem areaItemSelected;
    private float downX;
    private float downY;

    public MapView(Context context) {
        super(context);
        init(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        InputStream inputStream = getSvgInputStream(context);
        areaItems = new ArrayList<>();

        //parse path
        PathParser.pathParseFromSVGInputStream(inputStream, areaItems, AreaItem.class);

        paint = new Paint();
        areaItemSelected = null;
    }

    private InputStream getSvgInputStream(Context context) {
        return context.getResources().openRawResource(R.raw.hongkong_high);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                for (AreaItem areaItem : areaItems) {
                    boolean touch = areaItem.isTouch(downX, downY);
                    if (touch) {
                        areaItemSelected = areaItem;
                        break;
                    } else {
                        areaItemSelected = null;
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.scale(1.1f, 1.1f);
        for (AreaItem areaItem : areaItems) {
            boolean isSelected = areaItemSelected == areaItem;
            if (isSelected) continue;
            areaItem.draw(canvas, paint, false);
        }
        if (areaItemSelected != null) {
            areaItemSelected.draw(canvas, paint, true);
        }

    }
}
