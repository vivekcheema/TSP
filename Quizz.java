package com.learnprogramming.academy.tsp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Quizz extends AppCompatActivity {

    private Button StartButton,BookmarkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        StartButton = findViewById(R.id.StartButton);
        BookmarkBtn = findViewById(R.id.Bookmarks);


        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryI = new Intent(Quizz.this, Categories.class);
                startActivity(categoryI);
            }
        });

        BookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookmarkI = new Intent(Quizz.this, Bookmark.class);
                startActivity(bookmarkI);
            }
        });
    }
}