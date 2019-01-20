package com.example.java.clock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleEditActivity extends Activity {

    EditText mDateEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        mDateEdit = (EditText) findViewById(R.id.dateEdit);
        mTitleEdit = (EditText) findViewById(R.id.titleEdit);
        mDetailEdit = (EditText) findViewById(R.id.detailEdit);
        mDelete = (Button) findViewById(R.id.delete);

        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        if (scheduleId != -1) {
            Realm realm = Realm.getInstance(this);
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();
            Schedule schedule = results.first();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(schedule.getDate());
            mDateEdit.setText(date);
            mTitleEdit.setText(schedule.getTitle());
            mDetailEdit.setText(schedule.getDetail());
            mDelete.setVisibility(View.VISIBLE);
        }else {
            mDelete.setVisibility(View.INVISIBLE);
        }
    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            date = sdf.parse(mDateEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        if (scheduleId != -1) {
            Realm realm = Realm.getInstance(this);
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();

            realm.beginTransaction();
            Schedule schedule = results.first();
            schedule.setDate(date);
            schedule.setTitle(mTitleEdit.getText().toString());
            schedule.setDetail(mDetailEdit.getText().toString());
            realm.commitTransaction();

            Snackbar.make(findViewById(android.R.id.content),
                    "アップデートしました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();
        } else {
            Realm realm = Realm.getInstance(this);
            realm.beginTransaction();
            Number maxId = realm.where(Schedule.class).max("id");
            long nextId = 1;
            if (maxId != null) nextId = maxId.longValue() + 1;
            Schedule schedule = realm.createObject(Schedule.class);
            schedule.setId(nextId);
            schedule.setDate(date);
            schedule.setTitle(mTitleEdit.getText().toString());
            schedule.setDetail(mDetailEdit.getText().toString());
            realm.commitTransaction();
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void onDeleteTapped(View view) {

        long scheduleId = getIntent().getLongExtra("schedule_id", -1);
        if (scheduleId != -1) {
            Realm realm = Realm.getInstance(this);
            RealmResults<Schedule> results = realm.where(Schedule.class)
                    .equalTo("id", scheduleId).findAll();

            realm.beginTransaction();
            results.remove(0);
            realm.commitTransaction();

            Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
