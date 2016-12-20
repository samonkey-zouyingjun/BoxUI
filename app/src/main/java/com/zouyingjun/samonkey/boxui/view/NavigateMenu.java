package com.zouyingjun.samonkey.boxui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zouyingjun.samonkey.boxui.R;

/**
 * Created by Tony J on 2016/12/20.
 */

public class NavigateMenu extends RelativeLayout {

    private LinearLayout llnext,llpre,llback,llstop,llhelp;
    private ImageView ivnext,ivpre,ivback,ivstop,ivhelp;
    private TextView tvnext,tvpre,tvback,tvstop,tvhelp;

    public NavigateMenu(Context context) {
        super(context);
        View.inflate(context,R.layout.view_navigatemenu,this);
        initView();
    }

    public NavigateMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context,R.layout.view_navigatemenu,this);
        initView();
    }

    public NavigateMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context,R.layout.view_navigatemenu,this);
        initView();
    }


    public void setOnClickLisener(final onNextOnclickListener onNextOnclickListener, final onBackOnclickListener onBackOnclickListener,
                                  final onStopOnclickListener onStopOnclickListener, final onPreOnclickListener onPreOnclickListener,
                                  final onHelpOnclickListener onHelpOnclickListener) {
        final int textColorPre = Color.parseColor("#00BBEC");

        llnext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onNextOnclickListener!=null)
                    onNextOnclickListener.onNextClick();
                resetView();
                ivnext.setImageResource(R.drawable.next_pre);
                tvnext.setTextColor(textColorPre);
            }
        });

        llstop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onStopOnclickListener!=null)
                    onStopOnclickListener.onStopClick();
                resetView();
                ivstop.setImageResource(R.drawable.stop_pre);
                tvstop.setTextColor(textColorPre);
            }
        });
        llback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onBackOnclickListener!=null)
                    onBackOnclickListener.onBackClick();
                resetView();
                ivback.setImageResource(R.drawable.back_pre);
                tvback.setTextColor(textColorPre);
            }
        });
        llpre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onPreOnclickListener!=null)
                    onPreOnclickListener.onPreClick();
                resetView();
                ivpre.setImageResource(R.drawable.pervious_pre);
                tvpre.setTextColor(textColorPre);
            }
        });
        llhelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onHelpOnclickListener!=null)
                    onHelpOnclickListener.onHelpClick();
                resetView();
                ivhelp.setImageResource(R.drawable.help_pre);
                tvhelp.setTextColor(textColorPre);
            }
        });
    }

    private void initView() {
        llhelp = (LinearLayout) findViewById(R.id.ll_nm_help);
        llnext = (LinearLayout) findViewById(R.id.ll_nm_next);
        llpre = (LinearLayout) findViewById(R.id.ll_nm_previous);
        llback = (LinearLayout) findViewById(R.id.ll_nm_back);
        llstop = (LinearLayout) findViewById(R.id.ll_nm_stop);

        ivhelp = (ImageView) findViewById(R.id.iv_nm_help);
        ivnext = (ImageView) findViewById(R.id.iv_nm_next);
        ivpre = (ImageView) findViewById(R.id.iv_nm_previous);
        ivback = (ImageView) findViewById(R.id.iv_nm_back);
        ivstop = (ImageView) findViewById(R.id.iv_nm_stop);

        tvhelp = (TextView) findViewById(R.id.tv_nm_help);
        tvnext = (TextView) findViewById(R.id.tv_nm_next);
        tvpre = (TextView) findViewById(R.id.tv_nm_previous);
        tvback = (TextView) findViewById(R.id.tv_nm_back);
        tvstop = (TextView) findViewById(R.id.tv_nm_stop);
    }



    public interface onNextOnclickListener {
        public void onNextClick();
    }
    public interface onBackOnclickListener {
        public void onBackClick();
    }
    public interface onStopOnclickListener {
        public void onStopClick();
    }
    public interface onPreOnclickListener {
        public void onPreClick();
    }
    public interface onHelpOnclickListener {
        public void onHelpClick();
    }

    public void changeVisible(){
        int visibility = this.getVisibility();
        if(visibility == View.VISIBLE){
            this.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    R.anim.menu_out);
            this.setAnimation(animation);
        }else{
            this.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(),
                    R.anim.menu_in);
            this.setAnimation(animation);
        }
        resetView();
    }
    public void resetView(){
        int textColorNor = Color.parseColor("#F8F8F8");
        ivhelp.setImageResource(R.drawable.help_nor);
        ivnext.setImageResource(R.drawable.next_nor);
        ivpre.setImageResource(R.drawable.pervious_nor);
        ivstop.setImageResource(R.drawable.stop_nor);
        ivback.setImageResource(R.drawable.back_nor);


        tvhelp.setTextColor(textColorNor);
        tvnext.setTextColor(textColorNor);
        tvpre.setTextColor(textColorNor);
        tvstop.setTextColor(textColorNor);
        tvback.setTextColor(textColorNor);
    }

}
