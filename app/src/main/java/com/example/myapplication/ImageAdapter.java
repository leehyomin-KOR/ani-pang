package com.example.myapplication;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private android.content.Context context;
    private Integer[] img = new Integer[]{R.drawable.moon};

    public ImageAdapter(android.content.Context context) {this.context = context;}

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int i) {
        return img[i];
    }

    @Override
    public long getItemId(int i) {
        Exception e = new Exception();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = null;
        if(view == null)
            imageView = new ImageView(context);
        else
            imageView = (ImageView) view;

        imageView.setLayoutParams(new ViewGroup.LayoutParams(43, 43));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageResource(img[i]);

        return imageView;
    }
};
