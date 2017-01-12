package com.zouyingjun.samonkey.boxui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by Tony J on 2017/1/12.
 */

public class VerticalSeekBar2 extends SeekBar {

    private int upcount;

    public VerticalSeekBar2(Context context) {
        super(context);
    }

    public VerticalSeekBar2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        //将SeekBar转转90度
        c.rotate(-90);
        //将旋转后的视图移动回来
        c.translate(-getHeight(),0);
        Log.i("getHeight()",getHeight()+"");
        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:


                //获取滑动的距离
                upcount=getMax() - (int) (getMax() * event.getY() / getHeight());
                //设置进度
                setProgress(upcount);
                Log.i("Progress",getProgress()+"");
                //每次拖动SeekBar都会调用
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                Log.i("getWidth()",getWidth()+"");
                Log.i("getHeight()",getHeight()+"");
                break;
            case MotionEvent.ACTION_UP:
                onStopTrackingTouch();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public interface UpListener{
        void seekBarUp( VerticalSeekBar2 seekBar,int i);
    }

    private UpListener upListener;

    public void setUpListener(UpListener listener){
        upListener = listener;
    }
    void onStopTrackingTouch() {
        if (upListener != null) {
            upListener.seekBarUp(this,upcount);
        }
    }
}
