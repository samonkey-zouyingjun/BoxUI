package com.zouyingjun.samonkey.boxui.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zouyingjun.samonkey.boxui.R;

/**
 * Created by Tony J on 2017/1/10.
 */

public class MyDialog extends Dialog {
    private String message;
    private TextView mMessage;
    /**默认style*/
    public MyDialog(Context context) {
        super(context, R.style.CustomDialog);
        initView();
    }

    public MyDialog(Context context,String message){
        this(context);
        this.message = message;
    }


    private void initView() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount=0f;
//		lp.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        setCanceledOnTouchOutside(false);
        this.setContentView(R.layout.dialog_progress);
        mMessage = (TextView) findViewById(R.id.tvLoad);
        setShowMessage(message);
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public void setShowMessage(String msg)
    {
        if (!TextUtils.isEmpty(msg))
        {
            mMessage.setText("连接中...");
            mMessage.setVisibility(View.VISIBLE);
        }
        else
        {
            mMessage.setVisibility(View.GONE);
        }
    }
}
