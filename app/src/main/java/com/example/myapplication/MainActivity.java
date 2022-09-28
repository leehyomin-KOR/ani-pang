package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b_start = (Button) findViewById(R.id.start_button);
        b_start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), SubActivity.class));
            }
        });
    }

    public void set_buttonClick(View view){
        startActivity(new Intent(getApplicationContext(), Setting.class));
    }

//    public void rank_buttonClick(View view){
//        startActivity(new Intent(getApplicationContext(), Rank.class));
//    }
}