package com.zouyingjun.samonkey.boxui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zouyingjun.samonkey.boxui.R;

/**
 * Created by Tony J on 2017/1/11.
 */

public class GuideAdapter extends PagerAdapter {

    private SparseArray<ImageView> sArray = new SparseArray<>();

    int[] imgs = {
            R.drawable.choose,R.drawable.fullview,
            R.drawable.fullviewpic,R.drawable.game
    };
    Context context;
    public GuideAdapter (Context contenxt){
        this.context = contenxt;
    }


    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == sArray.get((Integer)(object));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(sArray.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = sArray.get(position);
        if(iv == null){
            iv = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_vp_guide,container,false);
            iv.setImageResource(imgs[position]);
            sArray.put(position,iv);
        }

        container.addView(iv);
        return position;
    }
}
