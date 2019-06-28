package com.example.crimewave;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ViewGroup mContainer;
    private InputMethodManager inputMethodManager;
    OpenFragment openFragment;

    public void setOpenFragment(OpenFragment openFragment) {
        this.openFragment = openFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),"Your favorite crimes are listed here",Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "onCreateView: called");
        fragview = inflater.inflate(R.layout.fragment_force, container, false);
        filteredlist = new ArrayList<>();
        mContainer = container;
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        getForces();
        recyclerView = fragview.findViewById(R.id.ForceRecyclerView);
        recyclerForceAdapter = new RecyclerForceAdapter(getContext(), forcelist,getActivity());
        recyclerView.setAdapter(recyclerForceAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tittle = fragview.findViewById(R.id.ForceTittle);
        search = fragview.findViewById(R.id.ForceSearch);
        toggle = fragview.findViewById(R.id.SearchToggle);

        search.setVisibility(View.GONE);
        tittle.setVisibility(View.VISIBLE);
        return fragview;
    }

    void getForces(){
        new GetForceAdapter().getForcesListCall().enqueue(new Callback<ArrayList<ForcesBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<ForcesBasic>> call, Response<ArrayList<ForcesBasic>> response) {
                forcelist = response.body();
                recyclerForceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ArrayList<ForcesBasic>> call, Throwable t) {

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"No Network connection !!!",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getForces();
                    }
                }).show();
                snackbar.show();
                Log.e(TAG, "onFailure: ", t);

            }
        });
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
                    inputMethodManager.hideSoftInputFromWindow(mContainer.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    toggle.setImageResource(R.drawable.ic_search);
                    recyclerForceAdapter.showlist(forcelist);
                    isSearching = false;
                } else {
                    tittle.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    search.setFocusableInTouchMode(true);
                    search.requestFocus();
                    inputMethodManager.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
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
