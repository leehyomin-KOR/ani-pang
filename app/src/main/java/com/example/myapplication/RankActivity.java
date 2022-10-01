package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class RankActivity extends AppCompatActivity {

    ArrayList<Rank> rank_data_list = new ArrayList<Rank>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rank);


        /*activity_rank.xml의 ListView에 어댑터를 이용해 rank_data_list(Rank객체 리스트)연결*/

        ListView rank_list_view = (ListView)findViewById(R.id.rank_list_view);

        try{  //랭크데이터를 열어 한줄씩 객체단위로 끊어서 rank_data_list에 저장
            BufferedReader br = new BufferedReader(new FileReader("/data/data/com.example.ani-pang/files/db.txt"));
            String lines = "";
            while((lines = br.readLine()) != null){
                String[] str = lines.split("-");
                rank_data_list.add(new Rank(str[0], Integer.parseInt(str[1]), str[2]));
            }
            br.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Stream Failure", Toast.LENGTH_SHORT).show();
        }

        /* 저장된 랭크 객체들을 점수기준으로 정렬 */
        Comparator<Rank> comparator = new Comparator<Rank>() {
            @Override
            public int compare(Rank r1, Rank r2) {
                return r2.getScore() - r1.getScore();
            }
        };
        Collections.sort(rank_data_list, comparator);


        RankAdapter rankAdapter = new RankAdapter(this, rank_data_list);
        rank_list_view.setAdapter(rankAdapter);

        /* 랭크 데이터를 정렬된 버전으로 덮어쓰기
           앱 강종을 생각해서 onCreate에 작성*/
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("/data/data/com.example.ani-pang/files/db.txt"));
            for (Rank rank : rank_data_list) {
                bw.write(rank.getPlayer_name() + "-"
                        + Integer.toString(rank.getScore()) + "-"
                        + rank.getClear_date() + "\n");
            }
            bw.flush();
            bw.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Stream Failure", Toast.LENGTH_SHORT).show();
        }

        /* 랭크화면에서 나가기 버튼 */
        Button rank_quit = findViewById(R.id.rank_quit);
        rank_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}



