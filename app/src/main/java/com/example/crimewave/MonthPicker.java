package com.example.crimewave;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class MonthPicker extends DialogFragment {

    public static final int MIN_YEAR = 2016;

    public interface sendMonthInterface{
        void sendMonth(String month,int reqCode);
    }

    public sendMonthInterface monthInterface;

    TextView warning;
    NumberPicker monthPicker;
    NumberPicker yearPicker;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Material_Light_Dialog_MinWidth);

        LayoutInflater inflater = getActivity().getLayoutInflater();


        View dialog = inflater.inflate(R.layout.month_picker,null);
        warning = dialog.findViewById(R.id.MonthpickerWarning);
        monthPicker =  dialog.findViewById(R.id.picker_month);
        yearPicker =  dialog.findViewById(R.id.picker_year);

        String[] MonthDisplay = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        monthPicker.setMaxValue(11);
        monthPicker.setMinValue(0);
        monthPicker.setDisplayedValues(MonthDisplay);
        monthPicker.setValue(3);

        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(2019);
        yearPicker.setValue(2019);
        builder.setTitle("Pick a Month");

        builder.setView(dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int month = monthPicker.getValue() + 1;
                        if(yearPicker.getValue() == 2016){
                            if(monthPicker.getValue()<4) {
                                monthInterface.sendMonth("2019-04",getTargetRequestCode());
                                return;
                            }
                        }
                        else if(yearPicker.getValue() == 2019){
                            if(monthPicker.getValue()>3) {
                                monthInterface.sendMonth("2019-04",getTargetRequestCode());
                                return;
                            }
                        }
                        monthInterface.sendMonth(yearPicker.getValue()+"-"+(month>9?month:"0"+month), getTargetRequestCode());
                    }
                })
                .setNegativeButton("Cancell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MonthPicker.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                warningHandler(newVal,monthPicker.getValue());
            }
        });

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                warningHandler(yearPicker.getValue(),newVal);
            }
        });
    }

    void warningHandler(int year,int month){
        if(year == 2016){
            if(month < 4 ) warning.setVisibility(View.VISIBLE);
            else warning.setVisibility(View.GONE);
        }
        else  if(year == 2019){
            if(month>3) warning.setVisibility(View.VISIBLE);
            else warning.setVisibility(View.GONE);
        }
        else warning.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            monthInterface = (sendMonthInterface) getTargetFragment();
        }catch (ClassCastException c){
            Log.e(TAG, "onAttach: ClassCastException", c);
        }
    }
}
