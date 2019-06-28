package com.example.crimewave;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.crimewave.Crimes.Crime;
import com.example.crimewave.Crimes.CrimeItemTouchHelper;
import com.example.crimewave.Crimes.CrimeRecyclerAdapter;
import com.example.crimewave.Crimes.FavCrimeDBHelper;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FavoriteCrimesFragment extends Fragment implements CrimeItemTouchHelper.CrimeItemTouchHelperListner {

    //views
    RecyclerView FavCrimeListRecView;
    Button SearchCrimeBtn;
    TextView Favempty;

    //ArrayLists
    ArrayList< Crime > Favcrimes;

    //DB helper
    FavCrimeDBHelper favCrimeDBHelper;

    //open fragment listener
    OpenFragment openFragment;

    //Recycler adapter
    CrimeRecyclerAdapter favCrimeRecyclerAdapter;

    //removed
    int removedIndex = -1;
    Crime removedCrime;

    public void setOpenFragment(OpenFragment openFragment) {
        this.openFragment = openFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Snackbar.make(getActivity().findViewById(android.R.id.content),"your Favorite crimes listed here",Snackbar.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.fragment_favorite,container,false);

        //init views
        FavCrimeListRecView = view.findViewById(R.id.FavCrimeRecView);
        SearchCrimeBtn = view.findViewById(R.id.SearchCrimeBtn);
        Favempty = view.findViewById(R.id.favoritesempty);

        //init DB helper
        favCrimeDBHelper = new FavCrimeDBHelper(getActivity());

        //init array list
        Favcrimes = new ArrayList<>();

        //init Recycler View
        favCrimeRecyclerAdapter = new CrimeRecyclerAdapter(getContext(),Favcrimes,getActivity(),true);
        FavCrimeListRecView.setAdapter(favCrimeRecyclerAdapter);
        FavCrimeListRecView.setItemAnimator(new DefaultItemAnimator());
        FavCrimeListRecView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        FavCrimeListRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //attach item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelper = new CrimeItemTouchHelper(0,ItemTouchHelper.RIGHT,this,null);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(FavCrimeListRecView);

        getFavCrimes();

        return view;
    }

    private void getFavCrimes(){
        Cursor cursor = favCrimeDBHelper.getAllFavCrimeCursor();
        int i = 0;
        while (cursor.moveToNext()){
            Gson gson = new Gson();
            Favcrimes.add(gson.fromJson(cursor.getString(1),Crime.class));
            favCrimeRecyclerAdapter.notifyItemInserted(i++);
        }
        if(Favcrimes.isEmpty()) Favempty.setVisibility(View.VISIBLE);
        else Favempty.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        SearchCrimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment.LaunchFragment(OpenFragment.SEARCH_CRIMES);
            }
        });
    }


    CountDownTimer countDownTimer;
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position, @Nullable Boolean isLatLng) {
        if(countDownTimer!=null)countDownTimer.cancel();
        if(removedCrime!=null)favCrimeDBHelper.deleteCrime(removedCrime);
        removedIndex = viewHolder.getAdapterPosition();
        removedCrime = Favcrimes.remove(removedIndex);
        favCrimeRecyclerAdapter.notifyItemRemoved(removedIndex);
        if(Favcrimes.isEmpty()) Favempty.setVisibility(View.VISIBLE);
        final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"Crime removed from Favorites "+5,Snackbar.LENGTH_INDEFINITE);
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int counter = (int)millisUntilFinished/1000;
                snackbar.setText("Crime removed from Favorites "+counter);
            }

            @Override
            public void onFinish() {
                snackbar.dismiss();
                favCrimeDBHelper.deleteCrime(removedCrime);
            }
        };
        countDownTimer.start();
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                Favcrimes.add(removedIndex,removedCrime);
                Favempty.setVisibility(View.GONE);
                favCrimeRecyclerAdapter.notifyItemInserted(removedIndex);
            }
        }).show();
    }
}
