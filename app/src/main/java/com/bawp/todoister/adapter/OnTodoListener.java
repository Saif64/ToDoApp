package com.bawp.todoister.adapter;

import com.bawp.todoister.model.Task;

public interface OnTodoListener {
    void onTodoClick(Task task);
    void ontodoRadioButtonClick(Task task);
}
