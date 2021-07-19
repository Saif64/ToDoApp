package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDataBase;

import java.util.List;

public class Repository{
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public Repository(Application application) {
        TaskRoomDataBase dataBase = TaskRoomDataBase.getDatabase(application);
        taskDao = dataBase.taskDao();
        allTasks = taskDao.getTasks();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public void insert(Task task){
        TaskRoomDataBase.databaseWriteExecutor.execute(() -> taskDao.insertTask(task));
    }
    public LiveData<List<Task>> get(long id){
        return taskDao.get(id);
    }
    public void update(Task task){
        TaskRoomDataBase.databaseWriteExecutor.execute(()-> taskDao.update(task));
    }
    public void delete(Task task){
        TaskRoomDataBase.databaseWriteExecutor.execute(()-> taskDao.delete(task));
    }
}
