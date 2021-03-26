package com.lahacks.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class CashOutPage extends AppCompatActivity {

    private TextView text_view_countdown;
    private Button button_start_pause;
    private Button button_refresh;
    private CountDownTimer countdown_timer;

    private long startTimeInMillis;
    private boolean timerRunning;
    private long timeLeftInMillis;
    private long endTime;
    private double startMinutes;
    private long millisLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out_page);

        text_view_countdown = findViewById(R.id.text_view_countdown);
        button_start_pause = findViewById(R.id.button_start_pause);
        button_refresh = findViewById(R.id.button_refresh);

        button_start_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        //Setting time
        // possibly where to add the additional minutes achieved?
        startMinutes = 2;
        millisLeft = (long) startMinutes * 60000; //turns minutes to milliseconds

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(minutesAdded){
                    newMins = whatever new minutes was added
                    oldMillisLeft = millisLeft;
                    millisLeft = (newMins*60000) + oldMillisLeft;
                }*/

                if (millisLeft == 0) {
                    Toast noMin = Toast.makeText(CashOutPage.this, "You have no minutes!", Toast.LENGTH_SHORT);
                    noMin.setGravity(Gravity.CENTER, 0, 0);
                    noMin.show();
                    return;

                    /*
                    //FOR TESTING...
                    long newMins = 2;
                    millisLeft = (newMins * 60000);
                    */
                }
                setTime(millisLeft);
            }
        });
    }

    //Setting time of timer...
    private void setTime(long milliseconds) {
        startTimeInMillis = milliseconds;
        resetTimer();
    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillis;

        countdown_timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                millisLeft = timeLeftInMillis; //updating the start time for refresh
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                millisLeft = 0; //NEW
                updateButtons();
            }
        }.start();

        timerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        countdown_timer.cancel();
        timerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        timeLeftInMillis = startTimeInMillis;
        updateCountDownText();
        updateButtons();
    }

    private void updateButtons() {
        if(timerRunning) {
            button_start_pause.setText("Pause");
            button_refresh.setVisibility(View.INVISIBLE);
        } else {
            button_start_pause.setText("Start");
            button_refresh.setVisibility(View.VISIBLE);
            if(timeLeftInMillis < 1000) {
                button_start_pause.setVisibility(View.INVISIBLE);
            } else {
                button_start_pause.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateCountDownText() {
        int hours = (int) (timeLeftInMillis/1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if(hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        text_view_countdown.setText(timeLeftFormatted);
    }

    //For configuration changes & when app is started/closed
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", millisLeft); //NEW - saves millisLeft..
        editor.putLong("timeLeft", timeLeftInMillis);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTime);

        editor.apply();

        if (countdown_timer != null) {
            countdown_timer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        timeLeftInMillis = prefs.getLong("timeLeft", startTimeInMillis); //default when app is started...
        millisLeft = prefs.getLong("millisLeft", timeLeftInMillis); //NEWWWW
        timerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if (timerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMillis = endTime - System.currentTimeMillis();
            if (timeLeftInMillis < 0) {
                timeLeftInMillis = 0;
                timerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }
}