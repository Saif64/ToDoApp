package com.bawp.todoister.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> taskList;
    private final OnTodoListener todoListener;

    public RecyclerViewAdapter(List<Task> taskList, OnTodoListener ontodoListener) {
        this.taskList = taskList;
        this.todoListener = ontodoListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        String formatted = Utils.formatDate(task.getDueDate());

        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_enabled}
        },
                new int[]{
                        Color.LTGRAY, //disabled
                        Utils.priorityColor(task)
        });

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
//        holder.radioButton.setButtonTintList(colorStateList);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip todayChip;
        OnTodoListener onTodoListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            this.onTodoListener = todoListener;

            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task currTask = taskList.get(getAdapterPosition());
            int id = v.getId();
            if (id == R.id.todo_row_layout){
                onTodoListener.onTodoClick(currTask);
            }
            else if (id == R.id.todo_radio_button){
                onTodoListener.ontodoRadioButtonClick(currTask);
            }
        }
    }
}
