package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterToDo;
    private ImageButton calenderButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonID;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calenderGroup;
    private Date dueDate;
    Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;
    private Priority priority;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calenderGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        enterToDo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeek = view.findViewById(R.id.next_week_chip);
        nextWeek.setOnClickListener(this);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.getIsEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterToDo.setText(task.getTask());
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);



        calenderButton.setOnClickListener(view12 -> {
            calenderGroup.setVisibility(
                    calenderGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
            );
            Utils.hideKeyboard(view12);
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
        });

        priorityButton.setOnClickListener(v13 -> {
                Utils.hideKeyboard(v13);
                priorityRadioGroup.setVisibility(
                        priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
                        );
                priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (priorityRadioGroup.getVisibility() == View.VISIBLE){
                        selectedButtonID = checkedId;
                        selectedRadioButton = view.findViewById(selectedButtonID);
                        if (selectedRadioButton.getId() == R.id.radioButton_high){
                            priority = Priority.HIGH;
                        }
                        else if (selectedRadioButton.getId() == R.id.radioButton_med){
                            priority = Priority.MEDIUM;
                        }
                        else if(selectedRadioButton.getId() == R.id.radioButton_low){
                            priority = Priority.LOW;
                        }
                        else{
                            priority = Priority.LOW;
                            Snackbar.make(saveButton, "priority low", BaseTransientBottomBar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    else {
                        priority = Priority.LOW;
                    }
                });
        });

        saveButton.setOnClickListener(v -> {
            String task = enterToDo.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null){
                Task myTask = new Task(task, priority,
                        dueDate, Calendar.getInstance().getTime(),
                        false);
                if (isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(priority);
                    updateTask.setDueDate(dueDate);
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setEdit(false);

                }else{
                TaskViewModel.insert(myTask);
                }
                if (this.isVisible()){
                    this.dismiss();
                }
            }else {
                Snackbar.make(saveButton, R.string.empty_field, BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.today_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
        }
        else if (id == R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
        }
        else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
        }
    }
}