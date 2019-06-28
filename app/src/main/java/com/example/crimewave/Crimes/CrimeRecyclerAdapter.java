package com.example.crimewave.Crimes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crimewave.R;

import java.util.ArrayList;

public class CrimeRecyclerAdapter extends RecyclerView.Adapter< CrimeRecyclerAdapter.CrimeViewHolder > {

    private static final String TAG = "CrimeRecyclerAdapter";
    private boolean isfav;
    Context crimeContext;
    ArrayList<Crime> Crimes;
    int ExpandedIndex = -1;
    FragmentActivity myActivity;

    public CrimeRecyclerAdapter(Context crimeContext, ArrayList < Crime > crimes, FragmentActivity activity, Boolean isFav) {
        this.crimeContext = crimeContext;
        this.isfav = isFav;
        Crimes = crimes;
        myActivity = activity;
    }

    public void refreshAdapter(ArrayList<Crime> newCrimes){
        Crimes = newCrimes;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CrimeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.crime_item,viewGroup,false);
        CrimeViewHolder crimeViewHolder = new CrimeViewHolder(view);
        return crimeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeViewHolder crimeViewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");
        crimeViewHolder.setViews(Crimes.get(i));
        crimeViewHolder.CrimeForeGround.setTag(i);
        if(i == ExpandedIndex)
            crimeViewHolder.CrimeDetails.setVisibility(View.VISIBLE);
        else crimeViewHolder.CrimeDetails.setVisibility(View.GONE);

        crimeViewHolder.CrimeForeGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curpos = Integer.parseInt(v.getTag().toString());
                if(ExpandedIndex==curpos) {
                    ExpandedIndex = -1;
                    notifyItemChanged(curpos);
                } else{
                    int old = ExpandedIndex;
                    ExpandedIndex = curpos;
                    notifyItemChanged(old);
                    notifyItemChanged(ExpandedIndex);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(Crimes==null) return 0;
        else return Crimes.size();
    }

    class CrimeViewHolder extends RecyclerView.ViewHolder {
        Crime mCrime;
        CardView CrimeForeGround;
        CardView CrimeDetails;
        TextView CrimeId;
        TextView CrimeDate;
        TextView CrimeCatogory;
        TextView CrimeStreet;
        TextView CrimePersistantid;
        TextView CrimeContext;
        TextView CrimeLocationType;
        TextView CrimeLocationSubtype;
        TextView CrimeOutcomeLabel;
        TextView CrimeOutcomeStatusCatogory;
        TextView CrimeOutcomeStatusdate;
        TextView CrimeOnMap;
        ImageView FavIcon;
        TextView FavText;
        CrimeViewHolder(@NonNull View itemView) {
            super(itemView);
            CrimeForeGround = itemView.findViewById(R.id.CrimeForeground);
            CrimeDetails = itemView.findViewById(R.id.CrimeDetails);
            CrimeId = itemView.findViewById(R.id.CrimeID);
            CrimeDate = itemView.findViewById(R.id.CrimeDate);
            CrimeCatogory = itemView.findViewById(R.id.CrimeCategory);
            CrimeStreet = itemView.findViewById(R.id.CrimeStreet);
            CrimePersistantid = itemView.findViewById(R.id.CrimePersistantId);
            CrimeContext = itemView.findViewById(R.id.CrimeContext);
            CrimeLocationType = itemView.findViewById(R.id.CrimeLocationType);
            CrimeLocationSubtype = itemView.findViewById(R.id.CrimeLocationSubType);
            CrimeOutcomeLabel = itemView.findViewById(R.id.CrimeOutcomeStatusLabel);
            CrimeOutcomeStatusCatogory = itemView.findViewById(R.id.CrimeOutcomeStatusCatogory);
            CrimeOutcomeStatusdate = itemView.findViewById(R.id.CrimeOutcomeStatusDate);
            CrimeOnMap = itemView.findViewById(R.id.CrimeOnMap);
            FavIcon = itemView.findViewById(R.id.FavIcon);
            FavText = itemView.findViewById(R.id.FavText);

            FavIcon.setImageResource(R.drawable.ic_star);
            FavText.setText("Add To Favorites");
            if(isfav) {
                FavIcon.setImageResource(R.drawable.ic_delete);
                FavText.setText("Remove From Favorites");
            }

        }

        void setViews(final Crime crime){
            mCrime = crime;
            CrimeId.setText("ID : "+crime.getId());
            CrimeDate.setText("Date : "+crime.getMonth());
            CrimeCatogory.setText(crime.getCategory());
            if(!(crime.location==null))CrimeStreet.setText(crime.getLocation().getStreet().getName());
            else CrimeStreet.setText("Street Not Available");
            if(!(crime.persistent_id==null) && !crime.persistent_id.equals("") && crime.persistent_id.length()>5) {
                CrimePersistantid.setFocusable(true);
                CrimePersistantid.setFocusableInTouchMode(true);
                CrimePersistantid.setText(crime.persistent_id);
                CrimePersistantid.setTag(crime.persistent_id);
                CrimePersistantid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar = Snackbar.make(myActivity.findViewById(android.R.id.content),"Pesristant ID Copied to ClipBoard",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        ClipData clipData = ClipData.newPlainText("Persistant Id",v.getTag().toString());
                        clipboardManager.setPrimaryClip(clipData);
                    }
                });
            }
            else CrimePersistantid.setText("Persistent Id Not Available");
            setText(CrimeContext,"Context : ",crime.getContext());
            setText(CrimeLocationType,"Location type : ",crime.getLocation_type());
            setText(CrimeLocationSubtype,"Location Subtype : ",crime.getLocation_subtype());
            if(crime.getOutcome_status()!=null ){
                CrimeOutcomeLabel.setText("Outcome Status : ");
                setText(CrimeOutcomeStatusCatogory,"",crime.getOutcome_status().getCategory());
                setText(CrimeOutcomeStatusdate,"Date of outcome : ",crime.getOutcome_status().getDate());
            }
            else {
                CrimeOutcomeStatusdate.setVisibility(View.GONE);
                CrimeOutcomeStatusCatogory.setVisibility(View.GONE);
                CrimeOutcomeLabel.setText("Outcome Status : Not Available");
            }
            String mapLink = "http://maps.google.com/maps?q=loc:" + crime.getLocation().getLatitude() + "," + crime.getLocation().longitude + " (" + crime.getCategory() + ")";
            CrimeOnMap.setTag(mapLink);
            CrimeOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    crimeContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString())));
                }
            });
        }
        private  void setText(TextView v,@Nullable String Prefix,@Nullable String Text){
            if(Text == null || Text.length()<3) v.setVisibility(View.GONE);
            else {
                v.setVisibility(View.VISIBLE);
                v.setText(Prefix+Text);
            }
        }
    }
}
