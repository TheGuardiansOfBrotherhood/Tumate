package com.infinitybreak.tumate.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;

import com.infinitybreak.tumate.R;

import library.minimize.com.chronometerpersist.ChronometerPersist;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    Button buttonStart;
    Button buttonStop;
    ProgressBar progressBar;
    ChronometerPersist chronometerPersist;
    SharedPreferences sharedPreferences;
    int stopCoffeeBreak = 0;
    int progressStatus = 0;
    int progress = 0;
    public static int TIME_MAX = 15;

    AsyncTask asyncTask;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        progressBar = (ProgressBar) findViewById(R.id.circle_progress_bar);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        buttonStart = (Button) findViewById(R.id.start_button);
        buttonStop = (Button) findViewById(R.id.stop_button);

        sharedPreferences = getSharedPreferences("ChronometerSample", MODE_PRIVATE);
        chronometerPersist = ChronometerPersist.getInstance(chronometer, sharedPreferences);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initThreadProgressBar(0, TIME_MAX);
                chronometerPersist.startChronometer();
            }
        });



        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopChronometer();
            }
        });



    }

    public void startChronometer(){
        chronometerPersist.startChronometer();
        initThreadProgressBar(0, TIME_MAX);
    }
    public void stopChronometer(){
        chronometerPersist.stopChronometer();
        progressBar.setProgress(0);
    }

    public void updateView(){


    }


    /*
    * Se 1000 mili segundo é 1 segundo
    * e 1 min é 60 segundo
    * então 25 min é 1500s
    * e 5 min é 300s
    *
    * */
    public void initThreadProgressBar(int timeCurrent, final int timeMax){

        progressStatus = timeCurrent;
        progressBar.setMax(timeMax);

        asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {
                while (progressStatus < TIME_MAX) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressStatus ++;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    public void run() {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);

                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

                        chronometerPersist.stopChronometer();
                        if(stopCoffeeBreak % 4 == 0){
                            TIME_MAX = 10;
                        }else if(stopCoffeeBreak % 2 == 0){
                            TIME_MAX = 5;
                        } else if(stopCoffeeBreak % 2 != 0){
                            TIME_MAX = 15;
                        }


                    }
                });
                return null;
            }
        }.execute();

        stopCoffeeBreak++;
    }

    @Override protected void onResume() {
        super.onResume();
        chronometerPersist.resumeState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
