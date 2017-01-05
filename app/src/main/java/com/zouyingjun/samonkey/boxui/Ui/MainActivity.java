package com.zouyingjun.samonkey.boxui.Ui;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zouyingjun.samonkey.boxui.R;
import com.zouyingjun.samonkey.boxui.dialog.Mydialog;
import com.zouyingjun.samonkey.boxui.view.NavigateMenu;

public class MainActivity extends BaseActivity{

    private NavigateMenu nm;
    private FrameLayout fmRight,fmLeft;
    private LinearLayout llCenter;
    private TextView tvStatus;

    private final int GESTURE_MODE_MOVE = 0;
    private final int GESTURE_MODE_MOVE_CLICK = 1;
    private final int GESTURE_MODE_CLICK = 2;
    private int GESTURE_MODE = -1;

    @Override
    protected void myOnCreate(Bundle arg0) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        super.initView();
        tvStatus = (TextView) findViewById(R.id.tv_main_status);
        llCenter = (LinearLayout) findViewById(R.id.ll_gesture_center);
        fmRight = (FrameLayout) findViewById(R.id.fl_gestrue_right);
        fmLeft = (FrameLayout) findViewById(R.id.fl_gestrue_left);
        nm = (NavigateMenu) findViewById(R.id.nm);
        nm.setOnClickLisener(new NavigateMenu.onNextOnclickListener() {
            @Override
            public void onNextClick() {
                toastStr("next");
                changeGestureMode(GESTURE_MODE_MOVE);
            }
        }, new NavigateMenu.onBackOnclickListener() {
            @Override
            public void onBackClick() {
                toastStr("back");
                changeGestureMode(GESTURE_MODE_MOVE_CLICK);
            }
        }, new NavigateMenu.onStopOnclickListener() {
            @Override
            public void onStopClick() {
                toastStr("stop");
            }
        }, new NavigateMenu.onPreOnclickListener() {
            @Override
            public void onPreClick() {
                toastStr("pre");
                changeGestureMode(GESTURE_MODE_CLICK);
            }
        }, new NavigateMenu.onHelpOnclickListener() {
            @Override
            public void onHelpClick() {
                toastStr("help");
            }
        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        final GestureDetector mGestureLeft = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                float x = e.getX();
                Log.e("TAP",String.valueOf(x));
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e("Scroll","distanceX:"+distanceX+"distanceY:"+distanceY);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        final GestureDetector mGestureRight = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                Log.e("TAP",String.valueOf(x)+"-------"+y);
                return super.onSingleTapConfirmed(e);
            }

        });
        fmRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureLeft.onTouchEvent(motionEvent);
                return true;
            }
        });
        fmLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureRight.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    public void onClick(View v){
        nm.changeVisible();
    }

    public void onClick1(View v){
        this.mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
        this.mDialog.setLeftOnclickListener(new Mydialog.onLeftOnclickListener() {
            @Override
            public void onLeftClick() {
                toastStr("APP");
            }
        });
        this.mDialog.setRightOnclickListener(new Mydialog.onRightOnclickListener() {
            @Override
            public void onRightClick() {
                toastStr("Retry");
            }
        });
    }

    public void onClick2(View v){
        this.mDialog.show(Mydialog.TEXT_IMG_DIALOG);
    }

    private void changeGestureMode(int gestureCode){
        if(gestureCode==GESTURE_MODE_MOVE){
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.GONE);
            fmRight.setVisibility(View.VISIBLE);
        }else if(gestureCode==GESTURE_MODE_MOVE_CLICK){
            llCenter.setVisibility(View.VISIBLE);
            fmLeft.setVisibility(View.VISIBLE);
            fmRight.setVisibility(View.VISIBLE);
        }else if(gestureCode == GESTURE_MODE_CLICK){
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.VISIBLE);
            fmRight.setVisibility(View.GONE);
        }
        GESTURE_MODE = gestureCode;
    }
}
