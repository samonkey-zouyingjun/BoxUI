package com.zouyingjun.samonkey.boxui.Ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zouyingjun.samonkey.boxui.dialog.Mydialog;

/**
 * Created by Tony J on 2016/12/20.
 */

public abstract class BaseActivity extends Activity {
    public Mydialog mDialog;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mDialog!=null)
        {
            mDialog.dismiss();
            mDialog=null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setSystemStatus();
        myOnCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }
    /**5.0以后会有透明条*/
    private void setSystemStatus() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |     WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * Activity.onCreate 需在此setContentView 必须实现
     * @param arg0
     */
    protected abstract void myOnCreate(Bundle arg0);

    /**
     * 初始化View  必须在fzOnCreate（setContentView）后
     *
     */
    protected void initView()
    {
        // TODO 初始化view
        mDialog = new Mydialog(this);
    }
    /**
     * 初始化事件监听
     */
    protected void initEvent()
    {
        // TODO 初始化事件监听
    }

    /**
     * 初始化数据，执行在主线程
     */
    protected void initData()
    {
        // TODO 初始化数据
    }

    /**获取对话框*/
    public  Mydialog getProgressDialog(){
        if (mDialog==null)
        {
            mDialog = new Mydialog(this);
        }
        return mDialog;
    }

    /**
     * 无传递参数简单跳转Activity方法
     * @param clazz 将要跳转的目的地
     * @param isClose 完成后是否关闭此类
     */
    public void startActivity(Class<?> clazz, boolean isClose){
        startActivity(new Intent(this, clazz));
        if (isClose) {
            finish();
        }
    }

    /**
     * 简单跳转Activity方法
     * @param intent 你懂得
     * @param isClose 完成后是否关闭此类
     */
    public void startActivity(Intent intent, boolean isClose){
        startActivity(intent);
        if (isClose) {
            finish();
        }
    }

    public void toastStr(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
