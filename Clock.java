package com.learnprogramming.academy.tsp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;


public class Clock extends MainActivity implements View.OnClickListener {
    private long timeCountInMilliSeconds = 60000;
    private enum TimerStatus {
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private EditText editTextQuestionNo;
    private TextView textViewTime;
    public ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    int time = 0;
    int question = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
// method call to initialize the views
        initViews();
// method call to initialize the listeners
        initListeners();
        editTextQuestionNo.setVisibility(View.VISIBLE);
    }
    /**
     * method to initialize the views
     */
    private void initViews() {
        progressBarCircle = findViewById(R.id.progressBarCircle);
        editTextMinute = findViewById(R.id.editTextMinute);
        editTextQuestionNo = findViewById(R.id.editTextQuestionNo);
        textViewTime = findViewById(R.id.textViewTime);
        imageViewReset = findViewById(R.id.imageViewReset);
        imageViewStartStop = findViewById(R.id.imageViewStartStop);

    }
    /**
     * method to initialize the click listeners
     */
    private void initListeners() {
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;
        }

    }
    /**
     * method to reset count down timer
     */
    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }
    /**
     * method to start and stop count down timer
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
// call to initialize the timer values
            Assigner();
            setTimerValues();
// call to initialize the progress bar values
            setProgressBarValues();
// showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
// changing play icon to stop icon
            imageViewStartStop.setImageResource(R.drawable.icon_stop);
// making edit text not editable
            editTextMinute.setEnabled(false);
// changing the timer status to started
            timerStatus = TimerStatus.STARTED;
// call to start the count down timer
            startCountDownTimer();
        } else {
// hiding the reset icon
            imageViewReset.setVisibility(View.GONE);
// changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.icon_start);
// making edit text editable
            editTextMinute.setEnabled(true);
// changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }
    /**
     * method to initialize the values for count down timer
     */

    private void setTimerValues() {

        if (!editTextMinute.getText().toString().isEmpty()) {
// fetching value from edit text and type cast to integer
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
// toast message to fill edit text
            Toast.makeText(getApplicationContext(), getString(R.string.message_minutes),
                    Toast.LENGTH_LONG).show();
        }
// assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }
    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                editTextQuestionNo.setText("Question:"+question);
                question--;
                if (question > 0){
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
            // call to initialize the progress bar values
                            setProgressBarValues();
            // hiding the reset icon
                            imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
                            imageViewStartStop.setImageResource(R.drawable.icon_start);
            // making edit text editable
                            editTextMinute.setEnabled(true);
            // changing the timer status to stopped
                            timerStatus = TimerStatus.STOPPED;
                            countDownTimer.start();
                        }
                    };
                    new Handler().postDelayed(r,1000);
                }
                else{
                    stopCountDownTimer();
                    question = 0;
                }
            }
        }.start();
        countDownTimer.start();
    }
    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }
    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }
    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {
        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
        return hms;
    }

    private void Assigner(){

        if (!editTextQuestionNo.getText().toString().isEmpty()) {
// fetching value from edit text and type cast to integer
            question = Integer.parseInt(editTextQuestionNo.getText().toString().trim());
        } else {
// toast message to fill edit text
           Toast.makeText(getApplicationContext(),getString(R.string.message_questions), Toast.LENGTH_LONG)
            .show();
        }

    }

}