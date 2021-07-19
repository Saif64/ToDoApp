package com.bawp.todoister.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.TaskDao;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskRoomDataBase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS = 4;
    public static final String DATABASE_NAME = "todoister_database";
    public static volatile TaskRoomDataBase INSTANCE;
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDatabaseCallBack =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(() ->{
                        //invoke Dao and write
                        TaskDao taskDao = INSTANCE.taskDao();
                        taskDao.deleteAll(); //clean state

                        //writing to our table
                    });
                }
            };

    public static  TaskRoomDataBase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (TaskRoomDataBase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    TaskRoomDataBase.class, DATABASE_NAME)
                    .addCallback(sRoomDatabaseCallBack)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract TaskDao taskDao();
}
