package com.example.crimewave;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crimewave.Force.ForcesBasic;
import com.example.crimewave.Force.GetForceAdapter;
import com.example.crimewave.Force.RecyclerForceAdapter;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForceFragment extends Fragment {
    private static final String TAG = "ForceFragment";
    private View fragview;
    private ArrayList<ForcesBasic> forcelist, filteredlist;
    private RecyclerView recyclerView;
    private RecyclerForceAdapter recyclerForceAdapter;
    private TextView tittle;
    private EditText search;
    private ImageView toggle;
    private boolean isSearching = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        fragview = inflater.inflate(R.layout.fragment_force, container, false);
        filteredlist = new ArrayList<>();
        new GetForceAdapter().getForcesListCall().enqueue(new Callback<ArrayList<ForcesBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<ForcesBasic>> call, Response<ArrayList<ForcesBasic>> response) {
                forcelist = response.body();
                recyclerForceAdapter = new RecyclerForceAdapter(getContext(), forcelist);
                recyclerView.setAdapter(recyclerForceAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

            @Override
            public void onFailure(Call<ArrayList<ForcesBasic>> call, Throwable t) {

                Toast.makeText(getActivity(),"cannot connect to server!!!",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onFailure: ", t);

            }
        });
        recyclerView = fragview.findViewById(R.id.ForceRecyclerView);
        recyclerForceAdapter = new RecyclerForceAdapter(getContext(), forcelist);
        recyclerView.setAdapter(recyclerForceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tittle = fragview.findViewById(R.id.ForceTittle);
        search = fragview.findViewById(R.id.ForceSearch);
        toggle = fragview.findViewById(R.id.SearchToggle);
        return fragview;
    }

    @Override
    public void onStart() {
        super.onStart();

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: toggle ");
                if (isSearching) {
                    tittle.setVisibility(View.VISIBLE);
                    search.setText("");
                    search.setVisibility(View.GONE);
                    toggle.setImageResource(R.drawable.ic_search);
                    recyclerForceAdapter.showlist(forcelist);
                    isSearching = false;
                } else {
                    tittle.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    toggle.setImageResource(R.drawable.ic_clear);
                    isSearching = true;
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filer(s.toString());
            }
        });


    }

    private void filer(String name) {
        filteredlist = new ArrayList<>();
        Log.d(TAG, "filer: called");
            for (ForcesBasic fb : forcelist) {
                if (fb.getName().toLowerCase().contains(name.toLowerCase())) {
                    Log.d(TAG, "filer: added"+fb);
                    filteredlist.add(fb);
                }
            }
        recyclerForceAdapter.showlist(filteredlist);
    }


}
