package com.example.myapplication;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SubActivity extends AppCompatActivity {
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private TextView plate;
    private int plateSize;
    private PangImageView[][] array;
    private PangPosition[][] positions;
    private ConstraintLayout layout;
    private TextView hideDustBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);
        layout = findViewById(R.id.constraint);
        hideDustBar = findViewById(R.id.game_hide_dust_bar);
        plate = findViewById(R.id.game_plate);
        array = new PangImageView[7][7];
        positions = new PangPosition[7][7];

        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                plateSize = plate.getWidth() / 7 * 7;

                ViewGroup.LayoutParams plateLayoutParams = plate.getLayoutParams();
                plateLayoutParams.width = plateSize;
                plateLayoutParams.height = plateSize;
                plate.setLayoutParams(plateLayoutParams);

                ViewGroup.LayoutParams hideBarLayoutParams = hideDustBar.getLayoutParams();
                hideBarLayoutParams.width = plateSize;
                hideBarLayoutParams.height = plateSize / 7;
                hideDustBar.setLayoutParams((hideBarLayoutParams));

                plate.post(new Runnable() {
                    @Override
                    public void run() {
                        setArray();
                    }
                });
            }
        };

        plate.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    private void setArray() {
        int division9 = plateSize / 7;

        for (int q = 0; q < array.length; q++) {
            for (int w = 0; w < array[q].length; w++) {
                if (array[q][w] == null) {
                    //먼지 포지션만을 저장하는 배열
                    positions[q][w] = new PangPosition((int) plate.getX() + division9 * w, (int) plate.getY() + division9 * q);

                    //실제 먼지가 저장되는 배열
                    array[q][w] = new PangImageView(SubActivity.this
                            , (int) plate.getX() + (division9 * w)
                            , (int) plate.getY() + (division9 * q)
                            , division9
                            , division9
                            , (int) (Math.random() * 7) + 1);

                    layout.addView(array[q][w]);
                    /*Log.i("##### x", "" + array[q][w].getX());
                    Log.i("##### ax", "" + array[q][w].getAbsoluteX());*/
                }
            }
        }
    }
}