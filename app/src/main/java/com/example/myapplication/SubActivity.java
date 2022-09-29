package com.example.myapplication;

import static com.google.android.material.internal.ViewUtils.removeOnGlobalLayoutListener;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private TextView plate;
    private int plateSize;
    private PangImageView[][] array;
    private PangPosition[][] positions;
    private ConstraintLayout layout;
    private TextView hideDustBar;
    private int highScore;
    private int userScore;
    private TextView userScoreView;
    private TextView highScoreView;
    private int gameStatus;
    private static final int BEFORE_THE_GAME_START = 0;
    private static final int GAME_PAUSED = 1;
    private static final int GAME_TERMINATED = 2;
    private static final int GAME_PLAYING = 3;
    private Handler handler;

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
                        /*
                        do {
                            setArray();
                        } while(checkArray());
                         */
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

    private boolean checkArray() {
        final ArrayList<String> removeList = new ArrayList<>();
        boolean flag = false; //리턴할 변수

        for(int q = 0; q < array.length; q++) {
            for(int w = 0; w < array[q].length; w++) {
                int verticalMin = q - 2 < 0 ? 0 : q - 2;
                int verticalMax = q + 2 >= array.length ? array.length - 1 : q + 2;
                int horizontalMin = w - 2 < 0 ? 0 : w - 2;
                int horizontalMax = w + 2 >= array.length ? array.length - 1 : w + 2;

                int count = 0;
                for(int e = verticalMin + 1; e <= verticalMax; e++) {
                    //y축 검사
                    if(array[e - 1][w].getType() == array[e][w].getType()) {
                        count++;
                    }
                    else {
                        count = 0;
                    }

                    if(count >= 2) {
                        flag = true;

                        removeList.add(q+","+w);
                        for(int r = q + 1 ; r <= verticalMax ; r++) {
                            if(array[q][w].getType() == array[r][w].getType()) {
                                /* if(layout.getViewWidget(array[r][w]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_pang);
                                    array[r][w].startAnimation(anim);
                                } */

                                layout.removeView(array[r][w]);
                                removeList.add(r+","+w);

                            } else {
                                break;
                            }
                        }

                        for(int r = q - 1 ; r >= verticalMin ; r--) {
                            if(array[q][w].getType() == array[r][w].getType()) {
                                /* if(layout.getViewWidget(array[r][w]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_pang);
                                    dustArray[r][w].startAnimation(anim);
                                } */

                                layout.removeView(array[r][w]);
                                removeList.add(r+","+w);
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }

                count = 0;
                for(int e = horizontalMin + 1 ; e <= horizontalMax ; e++) {
                    //가로 탐색
                    if(array[q][e - 1].getType() == array[q][e].getType()) {
                        count++;
                    } else {
                        count = 0;
                    }

                    if(count >= 2) {
                        flag = true;

                        removeList.add(q+","+w);
                        for(int r = w + 1 ; r <= horizontalMax ; r++) {
                            if(array[q][w].getType() == array[q][r].getType()) {
                                /* if(layout.getViewWidget(array[q][r]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_pang;
                                    dustArray[q][r].startAnimation(anim);
                                } */

                                layout.removeView(array[q][r]);
                                removeList.add(q+","+r);
                            } else {
                                break;
                            }
                        }

                        for(int r = w - 1 ; r >= horizontalMin ; r--) {
                            if(array[q][w].getType() == array[q][r].getType()) {
                                /* if(layout.getViewWidget(array[q][r]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_pang);
                                    dustArray[q][r].startAnimation(anim);
                                } */

                                layout.removeView(array[q][r]);
                                removeList.add(q+","+r);
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        return flag;
    }

    private boolean checkDustArray() {
        final ArrayList<String> removeList = new ArrayList<>();
        boolean flag = false; //리턴할 변수

        for(int q = 0 ; q < array.length ; q++) {
            for(int w = 0 ; w < array[q].length ; w++) {
                int verticalMin = q - 2 < 0 ? 0 : q - 2;
                int verticalMax = q + 2 >= array.length ? array.length - 1 : q + 2;
                int horizontalMin = w - 2 < 0 ? 0 : w - 2;
                int horizontalMax = w + 2 >= array.length ? array.length - 1 : w + 2;

                /*Log.i("현재 좌표", q+", "+w);
                Log.i("verticalMin", ""+verticalMin);
                Log.i("verticalMax", ""+verticalMax);
                Log.i("horizontalMin", ""+horizontalMin);
                Log.i("horizontalMax", ""+horizontalMax);*/

                int count = 0;
                for(int e = verticalMin + 1 ; e <= verticalMax ; e++) {
                    //세로 탐색
                    if(array[e - 1][w].getType() == array[e][w].getType()) {
                        count++;
                    } else {
                        count = 0;
                    }

                    if(count >= 2) {

                        flag = true;

                        removeList.add(q+","+w);
                        for(int r = q + 1 ; r <= verticalMax ; r++) {
                            if(array[q][w].getType() == array[r][w].getType()) {
/*                                if(layout.getViewWidget(array[r][w]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_dust);
                                    array[r][w].startAnimation(anim);
                                }*/

                                layout.removeView(array[r][w]);
                                removeList.add(r+","+w);

                            } else {
                                break;
                            }
                        }

                        for(int r = q - 1 ; r >= verticalMin ; r--) {
                            if(array[q][w].getType() == array[r][w].getType()) {
/*                                if(layout.getViewWidget(array[r][w]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_dust);
                                    array[r][w].startAnimation(anim);
                                }*/

                                layout.removeView(array[r][w]);
                                removeList.add(r+","+w);
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }

                count = 0;
                for(int e = horizontalMin + 1 ; e <= horizontalMax ; e++) {
                    //가로 탐색
                    if(array[q][e - 1].getType() == array[q][e].getType()) {
                        count++;
                    } else {
                        count = 0;
                    }

                    if(count >= 2) {
                        flag = true;

                        removeList.add(q+","+w);
                        for(int r = w + 1 ; r <= horizontalMax ; r++) {
                            if(array[q][w].getType() == array[q][r].getType()) {
/*                                if(layout.getViewWidget(array[q][r]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_dust);
                                    array[q][r].startAnimation(anim);
                                }*/

                                layout.removeView(array[q][r]);
                                removeList.add(q+","+r);
                            } else {
                                break;
                            }
                        }

                        for(int r = w - 1 ; r >= horizontalMin ; r--) {
                            if(array[q][w].getType() == array[q][r].getType()) {
/*                                if(layout.getViewWidget(array[q][r]) != null) {
                                    //리무브 애니메이션
                                    Animation anim = AnimationUtils.loadAnimation(this, R.anim.remove_dust);
                                    array[q][r].startAnimation(anim);
                                }*/

                                layout.removeView(array[q][r]);
                                removeList.add(q+","+r);
                            } else {
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        for(int q = 0 ; q < removeList.size() ; q++) {
            //실제 Array에서 먼지 삭제
            int i = Integer.parseInt(removeList.get(q).split(",")[0]);
            int j = Integer.parseInt(removeList.get(q).split(",")[1]);
            array[i][j] = null;
        }

        if(flag && gameStatus == GAME_PLAYING) {

            //점수 갱신
            userScore += removeList.size() * 10;
            highScore = highScore < userScore ? userScore : highScore;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    userScoreView.setText(""+String.format("%,d", userScore));
                    highScoreView.setText(""+String.format("%,d", highScore));
                }
            });
        }

        return flag;
    }



}