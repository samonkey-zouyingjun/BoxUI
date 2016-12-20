package com.zouyingjun.samonkey.boxui.Ui;

import android.os.Bundle;
import android.view.View;

import com.zouyingjun.samonkey.boxui.R;
import com.zouyingjun.samonkey.boxui.dialog.Mydialog;
import com.zouyingjun.samonkey.boxui.view.NavigateMenu;

public class MainActivity extends BaseActivity{

    private NavigateMenu nm;

   /* @Override
    protected void onResume() {
        *//**
         * 设置为横屏
         *//*
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }*/

    @Override
    protected void myOnCreate(Bundle arg0) {
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void initView() {
        super.initView();
        nm = (NavigateMenu) findViewById(R.id.nm);
        nm.setOnClickLisener(new NavigateMenu.onNextOnclickListener() {
            @Override
            public void onNextClick() {
                toastStr("next");
            }
        }, new NavigateMenu.onBackOnclickListener() {
            @Override
            public void onBackClick() {
                toastStr("back");
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
            }
        }, new NavigateMenu.onHelpOnclickListener() {
            @Override
            public void onHelpClick() {
                toastStr("help");
            }
        });
//        this.mDialog.setTitle("title!");
//        this.mDialog.setMessageStr("message!");
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

}
