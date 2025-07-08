package com.example.pomodoroapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private ProgressBar progressBar;
    private Button startButton, stopButton, resetButton, breakButton, infoButton, settingsButton;
    private CountDownTimer countDownTimer;
    private long timeRemainingInMillis;
    private boolean timerRunning;
    private long initialTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);
        breakButton = findViewById(R.id.breakButton);
        infoButton = findViewById(R.id.infoButton);
        settingsButton = findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.pomodoroapp.SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Load timer settings from preferences
        loadTimerSettings();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreak();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        updateCountDownText();
        updateProgressBar();
    }

    private void loadTimerSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("pomodoro_prefs", MODE_PRIVATE);
        int workTime = sharedPreferences.getInt("work_time", 25); // Default 25 minutes
        int breakTime = sharedPreferences.getInt("break_time", 5); // Default 5 minutes
        timeRemainingInMillis = workTime * 60 * 1000;
        initialTime = timeRemainingInMillis;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeRemainingInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingInMillis = millisUntilFinished;
                updateCountDownText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
        startButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        breakButton.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        breakButton.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        loadTimerSettings();
        updateCountDownText();
        updateProgressBar();
        if (timerRunning) {
            countDownTimer.cancel();
            startTimer();
        }
    }

    private void startBreak() {
        SharedPreferences sharedPreferences = getSharedPreferences("pomodoro_prefs", MODE_PRIVATE);
        int breakTime = sharedPreferences.getInt("break_time", 5); // Default 5 minutes
        timeRemainingInMillis = breakTime * 60 * 1000;
        initialTime = timeRemainingInMillis;
        updateCountDownText();
        updateProgressBar();
        if (timerRunning) {
            countDownTimer.cancel();
        }
        startTimer();
    }

    private void updateCountDownText() {
        long minutes = timeRemainingInMillis / 1000 / 60;
        long seconds = (timeRemainingInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void updateProgressBar() {
        progressBar.setMax((int) (initialTime / 1000)); // Set max value
        int progress = (int) (timeRemainingInMillis / 1000); // Calculate remaining time
        progressBar.setProgress(progress); // Update progress
    }

    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pomodoro Bilgi");
        builder.setMessage("Pomodoro tekniği, belirli süreli çalışma ve mola periyotlarına dayalı bir zaman yönetimi tekniğidir. " +
                "Başlat düğmesine basarak 25 dakikalık bir oturum başlatabilirsiniz. " +
                "Durdur düğmesine basarak zamanlayıcıyı duraklatabilirsiniz. " +
                "Sıfırla düğmesine basarak zamanlayıcıyı sıfırlayabilirsiniz. " +
                "Mola düğmesine basarak 5 dakikalık bir mola başlatabilirsiniz.");
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
