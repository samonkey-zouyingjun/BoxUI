package com.zouyingjun.samonkey.boxui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tony J on 2017/1/11.
 */

public class ButtomIndicator extends View {
    private float varwith,with;
    private int childCount = 4;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ButtomIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtomIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        with = w;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();

        paint.setColor(Color.parseColor("#01B0DA"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);

        Path p = new Path();
        p.moveTo(varwith,getHeight());
        p.lineTo(varwith+with*1.0f/childCount,getHeight());

        canvas.drawPath(p,paint);
        canvas.restore();
    }

    public void updata(int position,float offsize){
        //修改varwith
        varwith = (with*1.0f/childCount)*(position+offsize);

        //回调dispathDraw
        invalidate();
    }

    public void setChildCount (int count){
        childCount = count;
    }
}
