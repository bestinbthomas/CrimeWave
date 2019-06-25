package com.example.crimewave;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Calendar;

public class MonthPicker extends DialogFragment {
    public static final int MIN_YEAR = 2000;
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    private DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar calendar = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.month_picker,null);
        final NumberPicker monthPicker =  dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker =  dialog.findViewById(R.id.picker_year);

        String[] MonthDisplay = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        monthPicker.setMaxValue(11);
        monthPicker.setMinValue(0);
        monthPicker.setDisplayedValues(MonthDisplay);
        monthPicker.setValue(calendar.get(Calendar.MONTH));

        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(calendar.get(Calendar.YEAR));
        yearPicker.setValue(calendar.get(Calendar.YEAR));

        builder.setView(dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDateSet(null,yearPicker.getValue(),monthPicker.getValue(),0);
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
}
