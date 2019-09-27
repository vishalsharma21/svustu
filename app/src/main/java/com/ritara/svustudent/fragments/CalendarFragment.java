package com.ritara.svustudent.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ritara.svustudent.Login;
import com.ritara.svustudent.R;
import com.ritara.svustudent.utils.CalendarDB;
import com.ritara.svustudent.utils.CalendarRecyclerAdapter;
import com.ritara.svustudent.utils.SharedPreferences_SVU;
import com.ritara.svustudent.utils.UserModel;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CustomCalendarView calendarView;
    private int mHour;
    private int mMinute;
    private String time = "";
    private CalendarRecyclerAdapter adapter;
    private SharedPreferences_SVU sharedPreferences_main;
    private RecyclerView recTask;
    private ArrayList<UserModel> taskModel;
    private SimpleDateFormat df;
    private Date date;
    private CalendarDB calendarDB;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        if(SharedPreferences_SVU.getInstance(getActivity()).get_Logged()) {
            recTask = (RecyclerView) root.findViewById(R.id.recTask);
            calendarDB = new CalendarDB(getActivity());
            taskModel = new ArrayList<>();
            taskModel.addAll(calendarDB.getAllRecords());
            recTask.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new CalendarRecyclerAdapter(getActivity(), taskModel);
            recTask.setAdapter(adapter);

            calendarView = (CustomCalendarView) root.findViewById(R.id.calendar_view);
            Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
            calendarView.setFirstDayOfWeek(Calendar.MONDAY);
            calendarView.setShowOverflowDate(false);
            calendarView.refreshCalendar(currentCalendar);

            List<DayDecorator> decorators = new ArrayList<>();
            decorators.add(new DisabledColorDecorator());
            calendarView.setDecorators(decorators);
            calendarView.refreshCalendar(currentCalendar);

            calendarView.setCalendarListener(new CalendarListener() {
                @Override
                public void onDateSelected(Date date_) {
                    df = new SimpleDateFormat("dd-MM-yyyy");
                    date = date_;
                    taskModel.clear();
                    taskModel = new ArrayList<>();
                    taskModel.addAll(calendarDB.getRecordsByDate(df.format(date)));

                    recTask.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new CalendarRecyclerAdapter(getActivity(), taskModel);
                    recTask.setAdapter(adapter);
                }

                @Override
                public void onMonthChanged(Date date) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                }
            });

            root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        openDialog(df.format(date));
                    } catch (Exception e) {
                        Snackbar.make(view, "Please select date from calendar.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }else{
            startActivity(new Intent(getActivity(), Login.class));
        }
        return root;
    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
                dayView.setBackgroundColor(color);
            }
        }
    }

    public void openDialog(final String format) {
        final Dialog dialog = new Dialog(getActivity()); // Context, this, etc.
        dialog.setContentView(R.layout.dialog_demo);
        ((TextView)dialog.findViewById(R.id.txtHeader)).setText("Create Task for "+ format);

        final EditText dialog_info = dialog.findViewById(R.id.dialog_info);

        dialog.findViewById(R.id.dialog_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiemPicker();
            }
        });

        dialog.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String msg = dialog_info.getText().toString();

                if(!time.isEmpty() && msg.length()>0){

                    CalendarDB calendarDB = new CalendarDB(getActivity());

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();

                    long res = calendarDB.createRecords(ts, format, time, msg);

                    if(res>0){
                        Toast.makeText(getActivity(), "Task created successfully.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getActivity(), "Oops, something wrong happened.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Time and message are mandatory fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void tiemPicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        time = hourOfDay + ":" + minute;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}