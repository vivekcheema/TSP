package com.learnprogramming.academy.tsp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rGroup;
    private RadioButton rTimer;
    private RadioButton rQuiz;
    private RadioButton rSamplePapers;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rGroup= (RadioGroup) findViewById(R.id.rGroup);
        rTimer= (RadioButton) findViewById(R.id.rTimer);
        rSamplePapers= (RadioButton) findViewById(R.id.rSamplePapers);
        rQuiz = (RadioButton) findViewById(R.id.rQuiz);
        next = (Button) findViewById(R.id.Next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rTimer.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, Clock.class);
                    startActivity(intent);
                }
                else if (rSamplePapers.isChecked()) {
                    Intent intent1 = new Intent(MainActivity.this, imageSample.class);
                    startActivity(intent1);
                }
                else if (rQuiz.isChecked()){
                        Intent intent2 = new Intent(MainActivity.this, Quizz.class);
                        startActivity(intent2);
                }
            }
        });
    }
}