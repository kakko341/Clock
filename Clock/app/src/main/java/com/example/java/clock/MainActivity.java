package com.example.java.clock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    AnalogClock ac;
    TextClock tc;
    TextView tv;
    ViewFlipper vf;
    float x;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM dd\nyyyy", Locale.ENGLISH);
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        TextView tv = (TextView) findViewById(R.id.textCalendar);
        tv.setText(df.format(cl.getTime()));

        ac = new AnalogClock(this);
        tc = new TextClock(this);

        vf = (ViewFlipper) this.findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(this);

    }

    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN){
            x = e.getX();
        }
        else if (e.getAction() == MotionEvent.ACTION_UP){
            if(x-20 > e.getX()){
                vf.showNext();
            }
            else if (x+20 < e.getX()){
                vf.showPrevious();
            }
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(Menu.NONE, 0, 0, "アラーム");
        menu.add(Menu.NONE, 1, 1, "ストップウォッチ");
        menu.add(Menu.NONE, 2, 2, "スケジュール");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected (MenuItem mi){
        switch (mi.getItemId()){
            case 0:
                Intent it = new Intent(MainActivity.this, Alarm.class);
                startActivity(it);
                break;
            case 1:
                Intent it2 = new Intent(MainActivity.this, Stopwatch.class);
                startActivity(it2);
                break;
            case 2:
                Intent it3 = new Intent(MainActivity.this, Scheduler.class);
                startActivity(it3);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(mi);
    }
}