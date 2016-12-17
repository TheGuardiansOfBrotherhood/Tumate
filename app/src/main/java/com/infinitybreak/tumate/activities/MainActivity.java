package com.infinitybreak.tumate.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.infinitybreak.tumate.R;
import com.infinitybreak.tumate.adapters.TaskAdapter;
import com.infinitybreak.tumate.daos.TaskDAO;
import com.infinitybreak.tumate.entities.Task;
import com.infinitybreak.tumate.fragments.AddTaskDialogFragment;

import java.util.ArrayList;

import library.minimize.com.chronometerpersist.ChronometerPersist;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG_DIALOG_ADD_TASK = "addTask";

    Chronometer chronometer;
    ProgressBar progressBar;
    ChronometerPersist chronometerPersist;
    SharedPreferences sharedPreferences;
    int stopCoffeeBreak = 1;
    int progressStatus = 0;
    public static int TIME_MAX = 15;

    MyAsyncTask task;

    private RecyclerView mRecyclerView;
    private ArrayList<Task> mTasks = new ArrayList<>();
    private boolean mIsStarting = false;

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

        sharedPreferences = getSharedPreferences("ChronometerSample", MODE_PRIVATE);
        chronometerPersist = ChronometerPersist.getInstance(chronometer, sharedPreferences);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshList();
    }

    /*
    * Se 1000 mili segundo é 1 segundo
    * e 1 min é 60 segundo
    * então 25 min é 1500s
    * e 5 min é 300s
    * */
    public void initThreadProgressBar(Integer position) {
        chronometerPersist.startChronometer();
        progressStatus = 0;
        TIME_MAX = getTimeTotal();
        progressBar.setProgress(0);
        progressBar.setMax(TIME_MAX);
        task = new MyAsyncTask();
        task.execute(position);
    }

    public int getTimeTotal() {
        if (stopCoffeeBreak % 8 == 0) {
            TIME_MAX = 10;
        } else if(stopCoffeeBreak % 2 == 0) {
            TIME_MAX = 5;
        } else if(stopCoffeeBreak % 2 != 0) {
            TIME_MAX = 15;
        }
        stopCoffeeBreak++;
        return TIME_MAX;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        chronometerPersist.resumeState();
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            while (mIsStarting && progressStatus < TIME_MAX) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressStatus++;
                publishProgress(progressStatus);
            }
//            handler.post(new Runnable() {
//                public void run() {
//                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    v.vibrate(500);
//
//                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
//                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
//                }
//            });
            return params[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer position) {
            chronometerPersist.stopChronometer();
            progressBar.setProgress(0);
            if (mIsStarting && stopCoffeeBreak % 2 != 0) {
                addPomodoro(position);
            }
            if (mIsStarting) {
                initThreadProgressBar(position);
            }
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
        TaskAdapter adapter = new TaskAdapter(this, mTasks, this, mIsStarting);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        ImageButton imageButton = (ImageButton) view;
        if (!mIsStarting) {
            mIsStarting = true;
            imageButton.setImageResource(R.mipmap.ic_stop);
            Integer position = (Integer) imageButton.getTag();
            initThreadProgressBar(position);
        } else {
            mIsStarting = false;
            imageButton.setImageResource(R.mipmap.ic_play_arrow);
        }
    }

    private void showDialogAddTask() {
        DialogFragment dialogFragment = new AddTaskDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), TAG_DIALOG_ADD_TASK);
    }

    private void addPomodoro(Integer position) {
        Task task = mTasks.get(position);
        task.setRealized(task.getRealized() + 1);
        TaskDAO.getInstance(this).update(task);
        refreshList();
    }
}