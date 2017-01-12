package com.zouyingjun.samonkey.boxui.entity;

/**
 * Created by Tony J on 2017/1/11.
 */

public class Config {

//    public static int SERVER_PORT = 22223;//端口号
    public static int SERVER_PORT = 22225;//端口号

    /**
     * 发送端
     *
     * 光标 3002 x,y滑动（int） 3001 点击
     * 视频 x,y（int）
     * 游戏 3020 x,y (float) 0~1
     *
     *
     * 4001,ip 请求服务器ip
     * 3052 请求音量
     * 3042 视频进度
     * */
    public static final int SEND_CURSOR_SCROLL = 3002;
    public static final int SEND__CURSOR_FLICK = 3001 ;
    public static final int SEND_GAME = 3020;
    public static final int SEND_IP = 4001;

    public static final int SEND_VOLUME = 3052;//客户端请求音量进度3052,0.5
    public static final int SEND_PROGRESS = 3042;//客户端请求视频进度3042,12



    /**
     *接收端
     * 4000,ip 收服务器ip
     *
     * 3100 服务器关闭
     * 3011 光标
     * 3012 手势—视频—滑动
     * 3013 手势-游戏-点击
     * 3014 宜居 双模式
     *
     *
     * next 3062
     * pre 3061
     * pause 3060
     * pro 3042
     * volume 3052

     * progress 3040
     * volem 3051(0.5 10)
     * currnet 3041
     * */
    public static final int RECEIVE_IP = 4000;
    public static final int RECEIVE_SERVER_CLOSE = 3100;
    public static final int RECEIVE_MODE_CURSOR = 3011;
    public static final int RECEIVE_MODE_MOVIE_SCROLL = 3012;
    public static final int RECEIVE_MODE_MOVIE_FLICK = 3013;
    public static final int RECEIVE_MODE_DOUBLE = 3014;

    public static final int RECEIVE_MOVIE_NEXT = 3062;
    public static final int RECEIVE_MOVIE_PRE = 3061;
    public static final int RECEIVE_MOVIE_PAUSE = 3060;
    public static final int RECEIVE_PROGRESS_CURRENT = 3041;
    public static final int RECEIVE_PROGRESS_TOTAL = 3040;
    public static final int RECEIVE_VOLUME = 3051; //音量接收 3051,0.1


}
