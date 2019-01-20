package com.example.java.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class Scheduler extends Activity {

    ListView mListView;

    protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduler);

        mListView = (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Realm realm = Realm.getInstance(this);
        RealmResults<Schedule> schedules = realm.where(Schedule.class).findAllSorted("date");
        ScheduleAdapter adapter =
                new ScheduleAdapter(this, schedules, true);
        mListView.setAdapter(adapter);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Scheduler.this, ScheduleEditActivity.class));
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Schedule schedule = (Schedule) parent.getItemAtPosition(position);
                startActivity(new Intent(Scheduler.this, ScheduleEditActivity.class)
                .putExtra("schedule_id", schedule.getId()));
            }
        });
    }
}