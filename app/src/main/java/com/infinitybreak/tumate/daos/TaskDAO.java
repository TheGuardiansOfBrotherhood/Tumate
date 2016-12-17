package com.infinitybreak.tumate.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.infinitybreak.tumate.entities.Task;
import com.infinitybreak.tumate.enums.TaskStatus;
import com.infinitybreak.tumate.utils.PersistenceHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public static final String TABLE = "task";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String ESTIMATED = "estimated";
    public static final String REALIZED = "realized";
    public static final String STATUS = "status";

    public static final String SCRIPT_CREATE_TABLE_TASK = "CREATE TABLE " + TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT," + ESTIMATED + " INTEGER,"
            + REALIZED + " INTEGER," + STATUS + " INTEGER);";

    public static final String SCRIPT_DROP_TABLE_TASK = "DROP TABLE IF EXISTS " + TABLE;

    private SQLiteDatabase database = null;
    private static TaskDAO instance;

    public static TaskDAO getInstance(Context context) {
        if (instance == null)
            instance = new TaskDAO(context);
        return instance;
    }

    private TaskDAO(Context context) {
        PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        database = persistenceHelper.getWritableDatabase();
    }

    public ArrayList<Task> select() {
        ArrayList<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE;

        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int indexId = cursor.getColumnIndex(ID);
                    int indexName = cursor.getColumnIndex(NAME);
                    int indexEstimated = cursor.getColumnIndex(ESTIMATED);
                    int indexRealized = cursor.getColumnIndex(REALIZED);
                    int indexStatus = cursor.getColumnIndex(STATUS);

                    do {
                        Task task = new Task();
                        task.setId(cursor.getInt(indexId));
                        task.setName(cursor.getString(indexName));
                        task.setEstimated(cursor.getInt(indexEstimated));
                        task.setRealized(cursor.getInt(indexRealized));
                        task.setStatus(TaskStatus.values()[cursor.getInt(indexStatus)]);

                        tasks.add(task);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return tasks;
    }

    public void insert(Task task){
        ContentValues values = generateContentValuesTask(task);
        database.insert(TABLE, null, values);
    }

    public void update(Task task){
        ContentValues values = generateContentValuesTask(task);
        String[] id = {String.valueOf(task.getId())};
        database.update(TABLE, values, ID + " = ?", id);
    }

    public void delete(Task task){
        String[] id = {String.valueOf(task.getId())};
        database.delete(TABLE, ID + " = ?", id);
    }

    private ContentValues generateContentValuesTask(Task task){
        ContentValues values = new ContentValues();
        values.put(NAME, task.getName());
        values.put(ESTIMATED, task.getEstimated());
        values.put(REALIZED, task.getRealized());
        values.put(STATUS, task.getStatus().ordinal());
        return values;
    }
}
