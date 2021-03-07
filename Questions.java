package com.learnprogramming.academy.tsp;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Questions extends AppCompatActivity {

    public static final String FILE_NAME = "Quiz";
    public static final String KEY_NAME = "Questions";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView Question, NumIndicator;
    private FloatingActionButton BookmarkBtn;
    private LinearLayout OptionsCont;
    private Button Sharebtn, NextBtn;
    private int count = 0;
    private List<QuestionModel> list;
    private int position = 0;
    private int score = 0;
    private String category;
    private int setNo;
    private Dialog loading;

    private List<QuestionModel> bookmarksList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuestionposition;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Question = findViewById(R.id.Question);
        BookmarkBtn = findViewById(R.id.BookmarkBtn);
        OptionsCont = findViewById(R.id.OptionsCont);
        NumIndicator = findViewById(R.id.NumIndicator);
        Sharebtn = findViewById(R.id.ShareBtn);
        NextBtn = findViewById(R.id.NextBtn);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        BookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelMatch()) {
                    bookmarksList.remove(matchedQuestionposition);
                    BookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                } else {
                    bookmarksList.add(list.get(position));
                    BookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
            }
        });

        category = getIntent().getStringExtra("category");
        getIntent().getIntExtra("setNo", 1);

        loading = new Dialog(this);
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedcorners));
        loading.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loading.setCancelable(false);


        list = new ArrayList<>();

        loading.show();
        myRef.child("Sets").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    list.add(snapshot.getValue(QuestionModel.class));
                }
                if (list.size() > 0) {

                    for (int i = 0; i < 4; i++) {
                        OptionsCont.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {
                                checkAns((Button) v);
                            }
                        });

                        Sharebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String body = list.get(position).getQuestion() + "\n"+
                                        list.get(position).getOptionA() + "\n"+
                                        list.get(position).getOptionB() + "\n"+
                                        list.get(position).getOptionC() + "\n"+
                                        list.get(position).getOptionD();
                                        Intent shareI = new Intent(Intent.ACTION_SEND);
                                shareI.setType("text/plain");
                                shareI.putExtra(Intent.EXTRA_SUBJECT,"Quiz Challenge");
                                shareI.putExtra(Intent.EXTRA_TEXT,body);
                                startActivity(Intent.createChooser(shareI, "share via"));
                            }
                        });
                    }
                    playanim(Question, 0, list.get(position).getQuestion());
                    NextBtn.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            NextBtn.setEnabled(false);
                            NextBtn.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position == list.size()) {
                                Intent scoreintent = new Intent(Questions.this, Score.class);
                                scoreintent.putExtra("score", score);
                                scoreintent.putExtra("total", list.size());
                                startActivity(scoreintent);
                                finish();
                                return;
                            }
                            count = 0;
                            playanim(Question, 0, list.get(position).getQuestion());
                        }
                    });
                } else {
                    finish();
                    Toast.makeText(Questions.this, "No questions", Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Questions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loading.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        storebookmark();
    }

    private void playanim(final View view, final int value, final String data) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = list.get(position).getOptionA();
                    } else if (count == 1) {
                        option = list.get(position).getOptionB();

                    } else if (count == 2) {
                        option = list.get(position).getOptionC();

                    } else if (count == 3) {
                        option = list.get(position).getOptionD();

                    }
                    playanim(OptionsCont.getChildAt(count), 0, option);
                    count++;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        NumIndicator.setText(position + 1 + "/" + list.size());
                        if (modelMatch()) {
                            BookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        } else {
                            BookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                        }
                    } catch (ClassCastException e) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playanim(view, 1, data);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAns(Button selectedOpt) {
        enableOption(false);
        NextBtn.setEnabled(true);
        NextBtn.setAlpha(1);
        if (selectedOpt.getText().toString().equals(list.get(position).getCorrectAns())) {
            score++;
            selectedOpt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        } else {
            selectedOpt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button Correctopt = (Button) OptionsCont.findViewWithTag(list.get(position).getCorrectAns());
            Correctopt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            OptionsCont.getChildAt(i).setEnabled(enable);
            if (enable) {
                OptionsCont.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }

    }


    private boolean modelMatch() {
        boolean matched = false;
        int i = 0;
        for (QuestionModel model : bookmarksList) {
            i++;
            if (model.getQuestion().equals(list.get(position).getQuestion())
                    && model.getCorrectAns().equals(list.get(position).getCorrectAns())
                    && model.getSetno() == list.get(position).getSetno()) {
                matched = true;
                matchedQuestionposition = i;
            }
            i++;
        }
        return matched;
    }


    private void getBookmarks() {
        String json = preferences.getString(KEY_NAME, "");
        Type type = new TypeToken<List<QuestionModel>>() {
        }.getType();

        bookmarksList = gson.fromJson(json, type);

        if (bookmarksList == null) {
            bookmarksList = new ArrayList<>();
        }
    }


    private void storebookmark() {
        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME, json);
        editor.commit();
    }
}
