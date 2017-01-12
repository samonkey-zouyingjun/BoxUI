package com.zouyingjun.samonkey.boxui.Ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zouyingjun.samonkey.boxui.R;
import com.zouyingjun.samonkey.boxui.adapter.GuideAdapter;
import com.zouyingjun.samonkey.boxui.dialog.Mydialog;
import com.zouyingjun.samonkey.boxui.entity.UDPReciever;
import com.zouyingjun.samonkey.boxui.entity.UDPSendClient;
import com.zouyingjun.samonkey.boxui.entity.Utils;
import com.zouyingjun.samonkey.boxui.view.MyDialog;
import com.zouyingjun.samonkey.boxui.view.VerticalSeekBar2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    //ui
    private FrameLayout fmRightScroll, fmRightFlick, fmRightView, fmLeft, fmVp;
    private LinearLayout llCenter, llVolmue, llVideo;
    private TextView tvStatus, tvVideo1, tvVideo2, tvVpTitle;
    private VerticalSeekBar2 verticalSeekBar;
    private SeekBar seekBar;
    private Button btBack, btNext, btPre, btStop, btGuide, btVolume;
    private ViewPager mVp;
    private GuideAdapter mAdapter;
    private RadioGroup rg;
    //广播
    private UDPReciever reciever;
    private UDPSendClient client;
    private String SERVER_IP = "255.255.255.255";
    ExecutorService cachedThreadPool;
    private MyDialog myDialog;
    //模式
    private final int GESTURE_MODE_SCROLL = 0;
    private final int GESTURE_MODE_FLICK = 4;
    private final int GESTURE_MODE_DOUBLE = 1;
    private final int GESTURE_MODE_CURSOR = 2;
    private int GESTURE_MODE = -1;
    //发送频率
    private long mLastMillis;
    private int mDistanceX;
    private int mDistanceY;

    @Override
    protected void myOnCreate(Bundle arg0) {
        setContentView(R.layout.activity_main);
        recieverThread reciever = new recieverThread();
        reciever.start();
        cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                client = new UDPSendClient("255.255.255.255");
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        rg = (RadioGroup) findViewById(R.id.rg_main_indicatorId);
        btBack = (Button) findViewById(R.id.bt_back);
        btNext = (Button) findViewById(R.id.bt_next);
        btStop = (Button) findViewById(R.id.bt_stop);
        btPre = (Button) findViewById(R.id.bt_pre);
        btGuide = (Button) findViewById(R.id.bt_guide);
        btVolume = (Button) findViewById(R.id.bt_control);
        tvStatus = (TextView) findViewById(R.id.tv_main_status);
        tvVideo1 = (TextView) findViewById(R.id.tv_scroll_vidoe1);
        tvVideo2 = (TextView) findViewById(R.id.tv_scroll_vidoe2);
        tvVpTitle = (TextView) findViewById(R.id.tv_vp_guide);
        llCenter = (LinearLayout) findViewById(R.id.ll_gesture_center);
        llVolmue = (LinearLayout) findViewById(R.id.ll_scroll_volmue);
        llVideo = (LinearLayout) findViewById(R.id.ll_scroll_vidoe);
        fmRightScroll = (FrameLayout) findViewById(R.id.fl_gestrue_right);
        fmRightView = (FrameLayout) findViewById(R.id.fl_gestrue_right1);
        fmRightFlick = (FrameLayout) findViewById(R.id.fl_gestrue_right2);
        fmLeft = (FrameLayout) findViewById(R.id.fl_gestrue_left);
        fmVp = (FrameLayout) findViewById(R.id.fl_main_vp);
        seekBar = (SeekBar) findViewById(R.id.sb_scroll_vidoe);
        mVp = (ViewPager) findViewById(R.id.vp_main_guide);
        mAdapter = new GuideAdapter(this);
        mVp.setAdapter(mAdapter);
        /**ViewPager*/
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {
                    tvVpTitle.setVisibility(View.VISIBLE);
                    setVpTitle(position);
                } else {
                    if (tvVpTitle.getVisibility() == View.VISIBLE) {
                        tvVpTitle.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_out));
                        tvVpTitle.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) rg
                        .getChildAt(position))
                        .setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        /**seekBar*/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int current = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage(3042 + "," + current);
                    }
                });
            }
        });
        verticalSeekBar = (VerticalSeekBar2) findViewById(R.id.vs_scroll_volmue);
        verticalSeekBar.setUpListener(new VerticalSeekBar2.UpListener() {
            @Override
            public void seekBarUp(VerticalSeekBar2 seekBar, int i) {

                if (i < 0) i = 0;
                else if (i > 10) i = 10;
                final int finalNum = i;
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage(3052 + "," + finalNum / 10f);
                    }
                });
            }
        });

        /**Dialog*/
        myDialog = new MyDialog(this);
        mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
        this.mDialog.setLeftOnclickListener(new Mydialog.onLeftOnclickListener() {
            @Override
            public void onLeftClick() {
                onBackPressed();
            }
        });
        this.mDialog.setRightOnclickListener(new Mydialog.onRightOnclickListener() {
            @Override
            public void onRightClick() {
                mDialog.dismiss();
                myDialog.show();
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            for (int i = 0; i < 10; i++) {
                                if (SERVER_IP.equals("255.255.255.255"))
                                    client.sendMessage(4001 + "," + Utils.getWifiIp(MainActivity.this));
                                else break;
                                Thread.sleep(500);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog.dismiss();
                                    if (SERVER_IP.equals("255.255.255.255")) {
                                        mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
                                    }
                                }
                            });


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        /**RadioButton*/
        ((RadioButton) rg.getChildAt(0)).setChecked(true);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rBtn = (RadioButton)
                        group.findViewById(checkedId);
                int pos = rg.indexOfChild(rBtn);
                mVp.setCurrentItem(pos);
            }
        });
    }


    private void setVpTitle(int position) {
        String[] tils = {
                "光标移动使用指南",
                "全景视频使用指南",
                "全景图片使用指南",
                "游戏使用指南"
        };
        tvVpTitle.setText(tils[position]);
        tvVpTitle.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_in));
    }

    int widthPixels, heightPixels;

    @Override
    protected void initData() {
        super.initData();
        //获取屏幕宽高
        WindowManager windowManager = MainActivity.this.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        widthPixels = dm.widthPixels;
        heightPixels = dm.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                client.sendMessage(4001 + "," + Utils.getWifiIp(MainActivity.this));
            }
        });
    }

    /**
     * 发送端
     * 光标 3002 x,y滑动（int） 3001 点击
     * 视频 x,y（int）
     * 游戏 3020 x,y (float) 0~1
     */
    @Override
    protected void initEvent() {
        super.initEvent();
        final GestureDetector mGestureRight = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                final int x = (int) e.getX();
                final int y = (int) e.getY();
                if (GESTURE_MODE == GESTURE_MODE_DOUBLE) {
//                    Log.d("TAG", "onSingleTapConfirmed: " + 3030 + "," + 3001 + "," + x + "," + y);
                    cachedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            client.sendMessage(3030 + "," + 3001 + "," + x + "," + y);
                        }
                    });
                } else {
                    cachedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            client.sendMessage(3001 + "," + x + "," + y);
                        }
                    });
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, final float distanceX, final float distanceY) {
                //回调时间点
                final long time = System.currentTimeMillis();

                if (mDistanceX == 0 && mDistanceY == 0) {
                    mDistanceX = (int) Math.round(distanceX * 0.6);
                    mDistanceY = (int) Math.round(distanceY * 0.6);

                }
                if (time - mLastMillis > 10) {
                    //记录时间点
                    mLastMillis = System.currentTimeMillis();
                    /**线程安全*/
                    final int x = mDistanceX;
                    final int y = mDistanceY;

                    if (GESTURE_MODE == GESTURE_MODE_DOUBLE) {
//                        Log.e(TAG, "onScroll: "+ 3030+","+3002 + "," + -(int) distanceX + "," + -(int) distanceY);
                        cachedThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                client.sendMessage(3030 + "," + 3002 + "," + -x + "," + -y);
                            }
                        });
                    } else {
                        cachedThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                client.sendMessage(3002 + "," + -x + "," + -y);
                            }
                        });
                    }
                    //发送结束
                    mDistanceX = 0;
                    mDistanceY = 0;
                } else {
                    mDistanceX += (int) Math.round(distanceX * 0.6);
                    mDistanceY += (int) Math.round(distanceY * 0.6);
                }


                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        final GestureDetector mGestureSingleTap = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onDown(MotionEvent e) {
                final float x = e.getX();
                final float y = e.getY();
//                Log.e("TAG", "onSingleTapConfirmed: " + 3020 + ",0," + x / widthPixels + "," + y / heightPixels);

                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage(3020 + ",0," + x / widthPixels + "," + y / heightPixels);
                    }
                });
                return super.onDown(e);

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                final float x = e.getX();
                final float y = e.getY();

//                Log.e("TAG", "onSingleTapConfirmed: " + 3020 + ",1," + x / widthPixels + "," + y / heightPixels);

                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage(3020 + ",1," + x / widthPixels + "," + y / heightPixels);
                    }
                });
                return super.onSingleTapConfirmed(e);
            }

        });


        final GestureDetector mGestureScroll = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, final float distanceX, final float distanceY) {
                //回调时间点
                final long time = System.currentTimeMillis();

                if (mDistanceX == 0 && mDistanceY == 0) {
                    mDistanceX = (int) Math.round(distanceX * 0.6);
                    mDistanceY = (int) Math.round(distanceY * 0.6);
                }

                if (time - mLastMillis > 10) {
                    //记录时间点
                    mLastMillis = System.currentTimeMillis();
                    /**线程安全*/
                    final int x = mDistanceX;
                    final int y = mDistanceY;

                    if (GESTURE_MODE == GESTURE_MODE_DOUBLE) {
//                        Log.e(TAG, "onScroll: " + 3031 + "," + -x + "," + -y);
                        cachedThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                client.sendMessage(3031 + "," + -x + "," + -y);
                            }
                        });
                    } else {
                        cachedThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                client.sendMessage(-x + "," + -y);
                            }
                        });
                    }
                    //发送结束
                    mDistanceX = 0;
                    mDistanceY = 0;
                } else {
                    mDistanceX += (int) Math.round(distanceX * 0.6);
                    mDistanceY += (int) Math.round(distanceY * 0.6);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        fmRightScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureScroll.onTouchEvent(motionEvent);
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
        fmRightFlick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureSingleTap.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.bt_back:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage("" + 3003);
                    }
                });
                break;
            case R.id.bt_next:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage("" + 3062);
                    }
                });
                break;
            case R.id.bt_stop:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage("" + 3060);
                    }
                });

                break;
            case R.id.bt_pre:
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage("" + 3061);
                    }
                });
                break;
            case R.id.iv_vp_closed:
            case R.id.bt_guide:
                if (fmVp.getVisibility() == View.VISIBLE) {
                    fmVp.setVisibility(View.GONE);
                } else {
                    mVp.setCurrentItem(0, false);
                    fmVp.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.bt_control:
                if (llVolmue.getVisibility() == View.VISIBLE) {
                    llVolmue.setVisibility(View.GONE);
                    llVideo.setVisibility(View.GONE);
                } else {
                    llVolmue.setVisibility(View.VISIBLE);
                    llVideo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_main_status:
                if (SERVER_IP.equals("255.255.255.255")) {
                    mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
                }
                break;
        }
    }

    private void changeGestureMode(int gestureCode) {

        if (mDialog != null) {
            mDialog.dismiss();
        }

        if (gestureCode == GESTURE_MODE_SCROLL) {
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.GONE);
            fmRightScroll.setVisibility(View.VISIBLE);
            fmRightView.setVisibility(View.VISIBLE);
            llVolmue.setVisibility(View.VISIBLE);
            llVideo.setVisibility(View.VISIBLE);
            fmRightFlick.setVisibility(View.GONE);
            btPre.setVisibility(View.VISIBLE);
            btStop.setVisibility(View.VISIBLE);
            btNext.setVisibility(View.VISIBLE);
            btVolume.setVisibility(View.VISIBLE);
        } else if (gestureCode == GESTURE_MODE_DOUBLE) {
            llCenter.setVisibility(View.VISIBLE);
            fmLeft.setVisibility(View.VISIBLE);
            fmRightView.setVisibility(View.VISIBLE);
            llVolmue.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            fmRightScroll.setVisibility(View.VISIBLE);
            fmRightFlick.setVisibility(View.GONE);
            btPre.setVisibility(View.GONE);
            btStop.setVisibility(View.GONE);
            btNext.setVisibility(View.GONE);
            btVolume.setVisibility(View.GONE);
        } else if (gestureCode == GESTURE_MODE_CURSOR) {
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.VISIBLE);
            fmRightScroll.setVisibility(View.GONE);
            fmRightFlick.setVisibility(View.GONE);
            fmRightView.setVisibility(View.GONE);
            btPre.setVisibility(View.GONE);
            btStop.setVisibility(View.GONE);
            btNext.setVisibility(View.GONE);
            btVolume.setVisibility(View.GONE);
        } else if (gestureCode == GESTURE_MODE_FLICK) {
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.GONE);
            fmRightScroll.setVisibility(View.GONE);
            fmRightFlick.setVisibility(View.VISIBLE);
            fmRightView.setVisibility(View.VISIBLE);
            llVolmue.setVisibility(View.GONE);
            llVideo.setVisibility(View.GONE);
            btPre.setVisibility(View.GONE);
            btStop.setVisibility(View.GONE);
            btNext.setVisibility(View.GONE);
            btVolume.setVisibility(View.GONE);
        }
        GESTURE_MODE = gestureCode;
        if (SERVER_IP.equals("255.255.255.255") && mDialog != null) {
            tvStatus.setText("未连接");
            SERVER_IP = "255.255.255.255";
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        if (SERVER_IP.equals("255.255.255.255")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            tvStatus.setText("已连接");
        }
    }

    private class recieverThread extends Thread {
        @Override
        public void run() {
            super.run();
            reciever = new UDPReciever(new UDPReciever.ServerCallBack() {
                @Override
                public void callBack(final String data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handlerReciever(data);
                        }
                    });
                }
            });
            reciever.recieverBroadcast();
        }
    }


    /**
     * 接收端
     * 4000,ip 收服务器ip
     * 4001,ip 请求服务器ip
     * <p>
     * 3100 服务器关闭
     * 3011 光标
     * 3012 手势—视频—滑动
     * 3013 手势-游戏-点击
     * 3014 宜居 双模式
     * <p>
     * next 3062
     * pre 3061
     * pause 3060
     * progress current 3042
     * volume 3052
     * <p>
     * progress total 3040
     * volume cunrrent 3051(0.5 10)
     */

    public void handlerReciever(final String data) {
//        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        String sub = data.split(",")[0];
        if (sub != null) {
            if (sub.equals("4000")) {
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.destroyClient();
                        client = new UDPSendClient(data.split(",")[1]);
                    }
                });
//                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                SERVER_IP = data.split(",")[1];
                String volume = data.split(",")[2];
                if (volume != null)
                    verticalSeekBar.setProgress((int) (Float.parseFloat(volume) * 10));
                tvStatus.setText("已连接");
//                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
            } else if (sub.equals("3051")) {
//                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                verticalSeekBar.setProgress((int) (Float.parseFloat(data.split(",")[1]) * 10));
            } else if (sub.equals("3040")) {
                int postime = Integer.parseInt(data.split(",")[1]);
                seekBar.setMax(postime);
                tvVideo2.setText(Utils.formateTime(postime));
            } else if (sub.equals("3041")) {
                int currentTime = Integer.parseInt(data.split(",")[1]);
                seekBar.setProgress(currentTime);
                tvVideo1.setText(Utils.formateTime(currentTime));
            } else {
//                Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
                if ("3011".equals(data)) changeGestureMode(GESTURE_MODE_CURSOR);
                else if ("3012".equals(data)) changeGestureMode(GESTURE_MODE_SCROLL);
                else if ("3013".equals(data)) changeGestureMode(GESTURE_MODE_FLICK);
                else if ("3014".equals(data)) changeGestureMode(GESTURE_MODE_DOUBLE);
                else if ("3100".equals(data)) {
                    tvStatus.setText("未连接");
                    SERVER_IP = "255.255.255.255";
                    this.mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reciever.setFlag(false);
    }
}
