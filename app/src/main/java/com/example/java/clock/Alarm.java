package com.example.java.clock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Alarm extends Activity {

    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        this.context = this;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);

        update_text = (TextView) findViewById(R.id.updateText);

        final Calendar calendar = Calendar.getInstance();

        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);

        Button start_alarm = (Button) findViewById(R.id.button_on);

        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                int hour = alarmTimePicker.getHour();
                int minute = alarmTimePicker.getMinute();

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis());
                if (calendar.before(now))
                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                /*if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }*/

                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }

                setAlarmText(hour_string + ":" + minute_string + "に設定しました。");

                my_intent.putExtra("extra", "alarm on");

                pending_intent = PendingIntent.getBroadcast(Alarm.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

               /* alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        now.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending_intent);*/

                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pending_intent);

            }

        });

        Button stop_alarm = (Button) findViewById(R.id.button_off);

        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAlarmText("alarm off");
                alarmManager.cancel(pending_intent);
                my_intent.putExtra("extra", "alarm off");
                sendBroadcast(my_intent);

            }
        });

        Button return_button = (Button) findViewById(R.id.button_return);

        return_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                finish();
            }
        });

    }

    public void setAlarmText(String alarmText) {

        update_text.setText(alarmText);
    }

}