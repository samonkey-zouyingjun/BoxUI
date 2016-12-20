package com.zouyingjun.samonkey.boxui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zouyingjun.samonkey.boxui.R;

/**
 * Created by Tony J on 2016/12/20.
 */

public class Mydialog extends Dialog {

    private TextView tvtitle,tvmessage,tvmessage2;
    private Button btleft,btright;
    private String titleStr,leftStr,rightStr,messageStr;
    private LinearLayout llTextImg,llTextBtn;

    private onLeftOnclickListener leftOnclickListener;//取消按钮被点击了的监听器
    private onRightOnclickListener rightOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onLeftOnclickListener
     */
    public void setLeftOnclickListener(onLeftOnclickListener onLeftOnclickListener) {
        this.leftOnclickListener = onLeftOnclickListener;
    }

    public void setRightOnclickListener(onRightOnclickListener onRightOnclickListener) {
        this.rightOnclickListener = onRightOnclickListener;
    }

    private Context context;
    private View v;
    public Mydialog(Context context) {
        super(context,R.style.MyDialog);
        this.context = context;
        //初始化界面控件
        v = LayoutInflater.from(context).inflate(R.layout.mydialog, null);
        initView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(v);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);


        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }
    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置左边（默认APP）按钮被点击后，向外界提供监听
        btleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftOnclickListener != null) {
                    leftOnclickListener.onLeftClick();
                }
            }
        });
        //设置右边（默认Retry）按钮被点击后，向外界提供监听
        btright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightOnclickListener != null) {
                    rightOnclickListener.onRightClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            tvtitle.setText(titleStr);
        }
        if (messageStr != null) {
            tvmessage.setText(messageStr);
        }
        if (messageStr != null) {
            tvmessage2.setText(messageStr);
        }
        //如果设置按钮的文字
        if (leftStr != null) {
            btleft.setText(leftStr);
        }
        if (rightStr!= null) {
            btright.setText(rightStr);
        }
    }

    private void initView(View v) {
        llTextImg = (LinearLayout) v.findViewById(R.id.ll_dialog_in);
        llTextBtn = (LinearLayout) v.findViewById(R.id.ll_dialog_out);

        tvtitle  = (TextView) v.findViewById(R.id.tv_dialog_title);
        tvmessage2 = (TextView) v.findViewById(R.id.tv_dialog_message2);
        tvmessage = (TextView) v.findViewById(R.id.tv_dialog_message);
        btleft = (Button) v.findViewById(R.id.bt_dialog_left);
        btright = (Button) v.findViewById(R.id.bt_dialog_right);
    }

    public Mydialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Mydialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     *
     */
    public interface onLeftOnclickListener {
        public void onLeftClick();
    }

    public interface onRightOnclickListener {
        public void onRightClick();
    }
    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    public void setMessageStr(String messageStr) {
        this.messageStr = messageStr;
    }

    public void setRightStr(String rightStr) {
        this.rightStr = rightStr;
    }

    public void setLeftStr(String leftStr) {
        this.leftStr = leftStr;
    }

    public static int TEXT_IMG_DIALOG = 0;
    public static int TEXT_BUTTON_DIALOG = 1;

    public void show(int i) {
        if(i == TEXT_IMG_DIALOG){
            llTextImg.setVisibility(View.VISIBLE);
            llTextBtn.setVisibility(View.GONE);
        }else if(i == TEXT_BUTTON_DIALOG){
            llTextImg.setVisibility(View.GONE);
            llTextBtn.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams attributes = this.getWindow().getAttributes();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        attributes.width = (int) (metrics.widthPixels * 0.9);
        attributes.height = (int) (metrics.heightPixels * 0.9);
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.dimAmount = 0.5f;
        this.getWindow().setAttributes(attributes);
        show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {

        Window window =  this.getWindow();
        if (this != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        super.show();
    }

}
