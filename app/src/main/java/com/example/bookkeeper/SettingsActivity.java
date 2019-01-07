package com.example.bookkeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends MainActivity implements View.OnClickListener{

    CheckBox smsCheckBox;
    EditText countingDate;
    Button okButton;
    public static final String APP_PREFERENCES = "settings";
    //public static final String APP_PREFERENCES_SMS = "sms";
    public static final String APP_PREFERENCES_COUNTING = "counting";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        countingDate = (EditText) findViewById(R.id.countingDate);
//        smsCheckBox = (CheckBox) findViewById(R.id.checkBoxSms);
        settings =  getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        okButton = (Button) findViewById(R.id.okButtonSettings);
        okButton.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
         // Запоминаем данные
        int counting = Integer.parseInt(countingDate.getText().toString());
            if (counting > 28) {
                counting = 28;
            }
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putInt(APP_PREFERENCES_COUNTING, counting);
//        editor.clear();
//        editor.putBoolean(APP_PREFERENCES_SMS, smsCheckBox.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (settings.contains(APP_PREFERENCES_COUNTING)) {
            // Выводим на экран данные из настроек
            int data = settings.getInt(APP_PREFERENCES_COUNTING, 0);
            countingDate.setText(String.valueOf(data));
        }
//        if (settings.contains(APP_PREFERENCES_SMS)) {
//            smsCheckBox.setChecked(settings.getBoolean(APP_PREFERENCES_SMS, false));
//        }
    }

    public void onClick(View v) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

