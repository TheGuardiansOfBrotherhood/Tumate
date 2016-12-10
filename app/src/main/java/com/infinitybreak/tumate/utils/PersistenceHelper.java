package com.infinitybreak.tumate.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.infinitybreak.tumate.daos.TaskDAO;

public class PersistenceHelper extends SQLiteOpenHelper{

    public static final String DATABASE = "tumate.db3";
    public static final int VERSION = 1;

    private static PersistenceHelper instance;
    private Context context;

    private PersistenceHelper(Context context) {
        super(context, DATABASE, null, VERSION);
        this.context = context;
    }

    public static PersistenceHelper getInstance(Context context){
        if (instance == null)
            instance = new PersistenceHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TaskDAO.SCRIPT_CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TaskDAO.SCRIPT_DROP_TABLE_TASK);
        onCreate(db);
    }
}
