package com.example.java.clock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class Stopwatch extends Activity implements Runnable, View.OnClickListener {

    private long startTime;
    // 10 msec order
    private int period = 10;

    private TextView timerText;
    private Button startButton, stopButton;

    private Thread thread = null;
    private final Handler handler = new Handler();
    private volatile boolean stopRun = false;

    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SSS");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch);

        timerText = (TextView)findViewById(R.id.timer);
        timerText.setText(dataFormat.format(0));

        startButton = (Button)findViewById(R.id.start_button);
        stopButton = (Button)findViewById(R.id.stop_button);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == startButton){
            stopRun = false;
            thread = new Thread(this);
            thread.start();

            startTime = System.currentTimeMillis();

        }
        else{
            stopRun = true;
            thread = null;
            timerText.setText(dataFormat.format(0));
        }
    }

    @Override
    public void run() {

        while (!stopRun) {
            // sleep: period msec
            try {
                Thread.sleep(period);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                stopRun = true;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long endTime = System.currentTimeMillis();
                    // カウント時間 = 経過時間 - 開始時間
                    long diffTime = (endTime - startTime);

                    timerText.setText(dataFormat.format(diffTime));


                }
            });
        }
    }
}
