package com.example.myapplication;

import android.content.Context;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

public class PangImageView extends AppCompatImageView {
    private int type;
    private int[] location;

    public PangImageView(Context context, int x, int y, int width, int height, int type){
        super(context);

        setX(x);
        setY(y);
        setLayoutParams(new ViewGroup.LayoutParams(width, height));
        this.type = type;

        switch(type){
            case 1:
                setImageDrawable((getResources().getDrawable(R.drawable.piece1)));
                break;
            case 2:
                setImageDrawable((getResources().getDrawable(R.drawable.piece2)));
                break;
            case 3:
                setImageDrawable((getResources().getDrawable(R.drawable.piece3)));
                break;
            case 4:
                setImageDrawable((getResources().getDrawable(R.drawable.piece4)));
                break;
            case 5:
                setImageDrawable((getResources().getDrawable(R.drawable.piece5)));
                break;
            case 6:
                setImageDrawable((getResources().getDrawable(R.drawable.piece6)));
                break;
            case 7:
                setImageDrawable((getResources().getDrawable(R.drawable.piece7)));
                break;
        }
    }

    public int getType(){
        return type;
    }

    public int getAbsoluteX(){
        location = new int[2];
        this.getLocationOnScreen(location);
        return location[0];
    }

    public int getAbsoluteY(){
        location = new int[2];
        this.getLocationOnScreen(location);
        return location[1];
    }
}
