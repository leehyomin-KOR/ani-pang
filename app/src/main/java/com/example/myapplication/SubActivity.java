package com.example.myapplication;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private TextView gameStartMessage;
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
    private TextView currTime;

    // 게임 시간 관련
    private ProgressBar timerBar;
    private ImageView timerImg;
    private int timer; //순수 게임 플레이 타임
    private int stackedNumber; //게임을 종료하기위해 쌓이는 값
    private int targetNumber; //stackedNumber가 도달해야 하는 값 (stackedNumber와 targetNumber의 값이 같아지면 게임 종료)
    private boolean timerThreadController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);
        layout = findViewById(R.id.constraint);
        userScoreView = findViewById(R.id.game_score);
        highScoreView = findViewById(R.id.game_high_score);
        currTime = findViewById(R.id.currTime);
        hideDustBar = findViewById(R.id.game_hide_dust_bar);
        timerBar = findViewById(R.id.game_timer_bar);
        plate = findViewById(R.id.game_plate);
        array = new PangImageView[7][7];
        positions = new PangPosition[7][7];
        timerImg = findViewById(R.id.game_timer_img);
        gameStartMessage = findViewById(R.id.game_start_message);
        targetNumber = timerBar.getMax();
        handler = new Handler();

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

                        do {
                            do {
                                //겹치는게 없을 때까지 판을 셋팅
                                setArray();
                            } while(checkArray());
                        } while(!checkPossible());

                        basicSetting();
                        startGame();
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

    private boolean checkPossible() {
        for(int q = 0; q < array.length; q++) {
            for (int w = 0; w < array[q].length; w++) {
                int verticalMin = q == 0 ? 0 : q - 1;
                int verticalMax = q + 1 == array.length ? array.length - 1 : q + 1 ;
                int horizontalMin = w == 0 ? 0 : w - 1;
                int horizontalMax = w + 1 == array[q].length ? array[q].length - 1 : w + 1 ;

                if(verticalMax + 1 < array.length) {
                    if(array[q][w].getType() == array[q+1][horizontalMin].getType()) {
                        if(array[q][w].getType() == array[q+2][horizontalMin].getType() || array[q][w].getType() == array[q+2][w].getType())
                            return true;
                    }
                    if(array[q][w].getType() == array[q+1][w].getType()) {
                        if(array[q][w].getType() == array[q+2][horizontalMin].getType() || array[q][w].getType() == array[q+2][horizontalMax].getType())
                            return true;
                    }
                    if(array[q][w].getType() == array[q+1][horizontalMax].getType()) {
                        if(array[q][w].getType() == array[q+2][w].getType() || array[q][w].getType() == array[q+2][horizontalMax].getType())
                            return true;
                    }
                }

                if(horizontalMax + 1 < array[q].length) {
                    if(array[q][w].getType() == array[verticalMin][w+1].getType()) {
                        if(array[q][w].getType() == array[verticalMin][w+2].getType() || array[q][w].getType() == array[q][w+2].getType())
                            return true;
                    }
                    if(array[q][w].getType() == array[q][w+1].getType()) {
                        if(array[q][w].getType() == array[verticalMin][w+2].getType() || array[q][w].getType() == array[verticalMax][w+2].getType())
                            return true;
                    }
                    if(array[q][w].getType() == array[verticalMax][w+1].getType()) {
                        if(array[q][w].getType() == array[q][w+2].getType() || array[q][w].getType() == array[verticalMax][w+2].getType())
                            return true;
                    }
                }
            }
        }

        return false;
    }

    //게임 시작
    private void startGame() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //타이머바 셋팅
        timerBar.setProgress(targetNumber);

        //관련 변수 초기화
        timerThreadController = true; //타이머 쓰레드 컨트롤 변수
        gameStatus = GAME_PLAYING;

        //타이머 바와 Game Start 메시지 보이게 함
        timerBar.setVisibility(View.VISIBLE);
        gameStartMessage.setVisibility(View.VISIBLE);
        gameStartMessage.bringToFront();

        //글자 날아오는 애니메이션
        /*
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.ready_go_anim);
        gameStartMessage.startAnimation(animation);
         */

        //'Ready Go!!' 메시지 1.5초 뒤 invisible처리
        //1.5초 뒤 게임 타이머 시작
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //글자 사라지는 애니메이션
                /*
                Animation animation = AnimationUtils.loadAnimation(SubActivity.this, R.anim.ready_go_anim2);
                gameStartMessage.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        gameStartMessage.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                */

                gameStartMessage.setVisibility(View.INVISIBLE);

                startGameTimer();
                //touchStatus = true; //터치 가능
            }
        }, 1500);
    }

    //게임 타이머 시작
    private void startGameTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //타이머 루프
                while(timerThreadController) {
                    try {
                        Thread.sleep(100);
                        timer++; //순수 게임시간
                        stackedNumber++;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //0.1초 마다 타이머 바 갱신
                                timerBar.setProgress(targetNumber - stackedNumber);
                                currTime.setText(""+String.format("%,d", (targetNumber - stackedNumber)/10)+"초");
                            }
                        });

                        if(stackedNumber >= targetNumber) {
                            //게임 종료 조건
                            timerThreadController = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //게임 상태가 GAME_PLAYING 일때만 게임 종료로 넘어감
                //이유는 onDestory에서도 위의 타이머 루프를 빠져나오기 때문. 그 땐 endGame()이 실행되면 안됨
                if(gameStatus != GAME_PLAYING)  {
                    return;
                } else {
                    endGame();
                }
            }
        }).start();
    }

    //게임 종료
    private void endGame() {
        //기록 저장
        /*saveRecord();

        mediaPlayer.stop();
        touchStatus = false;
        gameStatus = GAME_TERMINATED;

        handler.post(new Runnable() {
            @Override
            public void run() {
                //게임 종료 다이얼로그 생성
                LayoutInflater inflater = LayoutInflater.from(SubActivity.this);
                final View dialogLayout = inflater.inflate(R.layout.game_result, null);
                final Dialog dialog = new Dialog(SubActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //다이얼로그 제목 삭제
                dialog.setContentView(dialogLayout);
                dialog.setCancelable(false);
                dialog.show();

                TextView score = dialogLayout.findViewById(R.id.result_score);
                TextView playTime = dialogLayout.findViewById(R.id.result_play_time);
                TextView player = dialogLayout.findViewById(R.id.result_player);
                ImageView dustCryingImage = dialogLayout.findViewById(R.id.result_crying_dust);
                ImageView backBtn = dialogLayout.findViewById(R.id.result_back_btn);
                ImageView replayBtn = dialogLayout.findViewById(R.id.result_replay_btn);
                ImageView newScore = dialogLayout.findViewById(R.id.result_new_high_score);

                score.setText(String.format("%,d", userScore));
                playTime.setText(String.format("%.1fs", (float)timer/10));
                player.setText(setting.getPlayerName());
                newScore.setVisibility(userScore == highScore ? View.VISIBLE : View.INVISIBLE);

                //종료 창 먼지 애니메이션
                //점수가 신기록이면 하트 애니메이션 신기록이 아니면 우는 애니메이션
                dustCryingImage.setBackgroundResource(userScore == highScore ? R.drawable.dust_heart_anim : R.drawable.dust_crying_anim);
                final AnimationDrawable anim = (AnimationDrawable)dustCryingImage.getBackground();
                anim.start();

                //종료 효과음 출력
                //신기록 여부에 따라 다른 효과음 출력
                if(effectSound)
                    soundPool.play(userScore == highScore ? gameEndSound2 : gameEndSound1, effectSoundVolume, effectSoundVolume,  1,  0,  1.0f);

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(effectSound)
                            soundPool.play(btnClick1, effectSoundVolume, effectSoundVolume,  1,  0,  1);

                        anim.stop();
                        dialog.dismiss();
                        finish();
                    }
                });

                replayBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(effectSound)
                            soundPool.play(btnClick1, effectSoundVolume, effectSoundVolume,  1,  0,  1);

                        anim.stop();
                        dialog.dismiss();
                        gameReplay();
                    }
                });
            }
        });*/
    }

    private void basicSetting() {
        //점수 셋팅
        userScore = 0;
        userScoreView.setText(""+String.format("%,d", userScore));
        highScoreView.setText(""+String.format("%,d", highScore));

        //미디어 플레이어 셋팅
        //setMediaPlayer();

        timer = 0;
        stackedNumber = 0;
    }
}