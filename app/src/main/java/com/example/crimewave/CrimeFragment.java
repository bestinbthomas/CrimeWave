package com.example.crimewave;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.crimewave.Crimes.Crime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CrimeFragment  extends Fragment {

    private static final String TAG = "CrimeFragment";
    View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_crime_at_location,container,false);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
       // ArrayList<Crime> crimes = new ArrayList<Crime>(gson.fromJson("",Crime[].class));
    //gson.fromJson("",Crime[].class);
    }


}
