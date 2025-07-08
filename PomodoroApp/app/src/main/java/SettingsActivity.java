package com.example.pomodoroapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText workTimeEditText;
    private EditText breakTimeEditText;
    private Button saveButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        workTimeEditText = findViewById(R.id.workTimeEditText);
        breakTimeEditText = findViewById(R.id.breakTimeEditText);
        saveButton = findViewById(R.id.saveButton);

        sharedPreferences = getSharedPreferences("pomodoro_prefs", MODE_PRIVATE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int workTime = Integer.parseInt(workTimeEditText.getText().toString());
                int breakTime = Integer.parseInt(breakTimeEditText.getText().toString());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("work_time", workTime);
                editor.putInt("break_time", breakTime);
                editor.apply();

                finish(); // Ayarlar kaydedildikten sonra bu ekranı kapat
            }
        });

        // Mevcut ayarları yükleyin
        int workTime = sharedPreferences.getInt("work_time", 25); // Varsayılan 25 dakika
        int breakTime = sharedPreferences.getInt("break_time", 5); // Varsayılan 5 dakika
        workTimeEditText.setText(String.valueOf(workTime));
        breakTimeEditText.setText(String.valueOf(breakTime));
    }
}
