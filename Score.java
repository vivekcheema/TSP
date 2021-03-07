package com.learnprogramming.academy.tsp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Score extends AppCompatActivity {


    private TextView Scored,Total;
    private Button DoneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Scored = findViewById(R.id.Scored);
        Total = findViewById(R.id.Total);
        DoneBtn = findViewById(R.id.DoneBtn);

        Scored.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        Total.setText("OUT OF "+String.valueOf(getIntent().getIntExtra("total",0)));

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}