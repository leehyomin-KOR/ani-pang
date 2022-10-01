package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b_start = (Button) findViewById(R.id.start_button);
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SubActivity.class));
            }
        });

        ImageView rank_button = findViewById(R.id.rank_button);
        rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_rank = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent_to_rank);
            }
        });

        File f_rank_data = new File("/data/data/com.example.myapplication/db.txt");
        if (!f_rank_data.exists()) {
            try {
                f_rank_data.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }



        public void set_buttonClick(View view){
            startActivity(new Intent(getApplicationContext(), Setting.class));
        }

}