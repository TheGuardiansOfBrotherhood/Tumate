package com.infinitybreak.tumate.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;

import com.infinitybreak.tumate.R;
import com.infinitybreak.tumate.adapters.TaskAdapter;
import com.infinitybreak.tumate.daos.TaskDAO;
import com.infinitybreak.tumate.entities.Task;
import com.infinitybreak.tumate.fragments.AddTaskDialogFragment;

import java.util.ArrayList;

import library.minimize.com.chronometerpersist.ChronometerPersist;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_DIALOG_ADD_TASK = "addTask";

    Chronometer chronometer;
    Button buttonStart;
    Button buttonStop;
    ProgressBar progressBar;
    ChronometerPersist chronometerPersist;
    SharedPreferences sharedPreferences;
    int stopCoffeeBreak = 1;
    int progressStatus = 0;
    int progress = 0;
    public static int TIME_MAX = 15;
    boolean isRunningAsync = true;

    MyAsyncTask task;
    private Handler handler = new Handler();

    private RecyclerView mRecyclerView;
    private ArrayList<Task> mTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddTask();
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
                initThreadProgressBar(0);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopChronometer();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshList();
    }

    public void startChronometer() {
        chronometerPersist.startChronometer();
        initThreadProgressBar(0);
    }

    public void stopChronometer() {
        chronometerPersist.stopChronometer();
        isRunningAsync = false;
        task = null;
    }

    /*
    * Se 1000 mili segundo é 1 segundo
    * e 1 min é 60 segundo
    * então 25 min é 1500s
    * e 5 min é 300s
    * */
    public void initThreadProgressBar(int timeCurrent){
        chronometerPersist.startChronometer();
        isRunningAsync = true;
        progressStatus = timeCurrent;
        TIME_MAX = getTimeTotal();
        progressBar.setProgress(0);
        progressBar.setMax(TIME_MAX);
        task = new MyAsyncTask();
        task.execute(progressStatus);
    }

    public int getTimeTotal() {
        stopCoffeeBreak++;
        if (stopCoffeeBreak % 8 == 0) {
            TIME_MAX = 10;
            stopCoffeeBreak = 1;
        } else if(stopCoffeeBreak % 2 == 0) {
            TIME_MAX = 15;
        } else if(stopCoffeeBreak % 2 != 0) {
            TIME_MAX = 5;
        }
        return TIME_MAX;
    }

    @Override
    protected void onResume() {
        super.onResume();
        chronometerPersist.resumeState();
    }

    private class MyAsyncTask extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... integers) {

            while (isRunningAsync && progressStatus < TIME_MAX) {
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
//                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    v.vibrate(500);

//                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
//                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chronometerPersist.stopChronometer();
            isRunningAsync = false;
            initThreadProgressBar(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void refreshList() {
        mTasks.clear();
        mTasks = TaskDAO.getInstance(this).select();
        TaskAdapter adapter = new TaskAdapter(this, mTasks, null, null);
        mRecyclerView.setAdapter(adapter);
    }

    private void showDialogAddTask() {
        DialogFragment dialogFragment = new AddTaskDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), TAG_DIALOG_ADD_TASK);
    }
}