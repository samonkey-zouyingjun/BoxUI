package com.zouyingjun.samonkey.boxui.Ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zouyingjun.samonkey.boxui.R;
import com.zouyingjun.samonkey.boxui.dialog.Mydialog;
import com.zouyingjun.samonkey.boxui.entity.UDPReciever;
import com.zouyingjun.samonkey.boxui.entity.UDPSendClient;
import com.zouyingjun.samonkey.boxui.entity.Utils;
import com.zouyingjun.samonkey.boxui.view.NavigateMenu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {
    //ui
    private NavigateMenu nm;
    private FrameLayout fmRightScroll, fmRightFlick, fmRightView, fmLeft;
    private LinearLayout llCenter;
    private TextView tvStatus;
    //广播
    private UDPReciever reciever;
    private UDPSendClient client;
    private String SERVER_IP = "255.255.255.255";
    ExecutorService cachedThreadPool;
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
        tvStatus = (TextView) findViewById(R.id.tv_main_status);
        llCenter = (LinearLayout) findViewById(R.id.ll_gesture_center);
        fmRightScroll = (FrameLayout) findViewById(R.id.fl_gestrue_right);
        fmRightView = (FrameLayout) findViewById(R.id.fl_gestrue_right1);
        fmRightFlick = (FrameLayout) findViewById(R.id.fl_gestrue_right2);
        fmLeft = (FrameLayout) findViewById(R.id.fl_gestrue_left);
        nm = (NavigateMenu) findViewById(R.id.nm);
        nm.setOnClickLisener(new NavigateMenu.onNextOnclickListener() {
            @Override
            public void onNextClick() {
                toastStr("next");
            }
        }, new NavigateMenu.onBackOnclickListener() {
            @Override
            public void onBackClick() {
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage("" + 3003);
                    }
                });
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
                cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        client.sendMessage(4001 + "," + Utils.getWifiIp(MainActivity.this));
                    }
                });
            }
        });
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
                    Log.d("TAG", "onSingleTapConfirmed: " + 3030 + "," + 3001 + "," + x + "," + y);
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

                if(mDistanceX == 0&& mDistanceY == 0){
                    mDistanceX =(int) distanceX;
                    mDistanceY =(int) distanceY;

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
                    mDistanceX += (int) distanceX;
                    mDistanceY += (int) distanceY;
                }


                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        final GestureDetector mGestureSingleTap = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onDown(MotionEvent e) {
                final float x = e.getX();
                final float y = e.getY();
                Log.e("TAG", "onSingleTapConfirmed: " + 3020 + ",0," + x / widthPixels + "," + y / heightPixels);

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

                Log.e("TAG", "onSingleTapConfirmed: " + 3020 + ",1," + x / widthPixels + "," + y / heightPixels);

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

                if(mDistanceX == 0&& mDistanceY == 0){
                    mDistanceX = (int) distanceX;
                    mDistanceY = (int) distanceY;
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
                    mDistanceX += (int) distanceX;
                    mDistanceY += (int) distanceY;
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
        nm.changeVisible();
    }


    public void onClick2(View v) {
        this.mDialog.show(Mydialog.TEXT_IMG_DIALOG);
    }

    private void changeGestureMode(int gestureCode) {

        if(mDialog!=null){
            mDialog.dismiss();
        }

        if (gestureCode == GESTURE_MODE_SCROLL) {
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.GONE);
            fmRightScroll.setVisibility(View.VISIBLE);
            fmRightView.setVisibility(View.VISIBLE);
            fmRightFlick.setVisibility(View.GONE);
        } else if (gestureCode == GESTURE_MODE_DOUBLE) {
            llCenter.setVisibility(View.VISIBLE);
            fmLeft.setVisibility(View.VISIBLE);
            fmRightView.setVisibility(View.VISIBLE);
            fmRightScroll.setVisibility(View.VISIBLE);
            fmRightFlick.setVisibility(View.GONE);
        } else if (gestureCode == GESTURE_MODE_CURSOR) {
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.VISIBLE);
            fmRightScroll.setVisibility(View.GONE);
            fmRightFlick.setVisibility(View.GONE);
            fmRightView.setVisibility(View.GONE);
        } else if (gestureCode == GESTURE_MODE_FLICK) {
            llCenter.setVisibility(View.GONE);
            fmLeft.setVisibility(View.GONE);
            fmRightScroll.setVisibility(View.GONE);
            fmRightFlick.setVisibility(View.VISIBLE);
            fmRightView.setVisibility(View.VISIBLE);
        }
        GESTURE_MODE = gestureCode;
        if (SERVER_IP.equals("255.255.255.255") && mDialog != null) {
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
     * 3014
     */
    public void handlerReciever(final String data) {
//        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        String sub = data.split(",")[0];
        if (sub != null && sub.equals("4000")) {
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    client.destroyClient();
                    client = new UDPSendClient(data.split(",")[1]);
                }
            });
            SERVER_IP = data.split(",")[1];
            tvStatus.setText("已连接");
        } else {
            if ("3011".equals(data)) changeGestureMode(GESTURE_MODE_CURSOR);
            else if ("3012".equals(data)) changeGestureMode(GESTURE_MODE_SCROLL);
            else if ("3013".equals(data)) changeGestureMode(GESTURE_MODE_FLICK);
            else if ("3014".equals(data)) changeGestureMode(GESTURE_MODE_DOUBLE);
            else if ("3100".equals(data)) {
                tvStatus.setText("未连接");
                this.mDialog.show(Mydialog.TEXT_BUTTON_DIALOG);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reciever.setFlag(false);
    }

}
