package com.example.crimewave.Force;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crimewave.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerForceAdapter extends RecyclerView.Adapter<RecyclerForceAdapter.ViewHolder> {
    private static final String TAG = "RecyclerForceAdapter";

    private ArrayList<SpecificForce> SpForces;
    private Context mContext;
    private int detailsShowAt = -1;
    private FragmentActivity activity;


    public RecyclerForceAdapter(Context context, @Nullable ArrayList<SpecificForce> forces, FragmentActivity activity) {
        this.activity = activity;
        Log.d(TAG, "RecyclerForceAdapter: constructr called with forces" + forces);
        SpForces = forces;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.force_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        Log.d(TAG, "onCreateViewHolder: called");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //Log.d(TAG, "onBindViewHolder: called");
        viewHolder.setValues(SpForces.get(i));
        viewHolder.forceCard.setTag(i);
        if(detailsShowAt==i)viewHolder.forceExtraLayout.setVisibility(View.VISIBLE);
        else viewHolder.forceExtraLayout.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        int a;
        if (SpForces == null) a = 0;
        else if (SpForces.isEmpty()) a = 0;
        else a = SpForces.size();
        return a;
    }

    public void showlist(ArrayList<SpecificForce> filteredlist) {
        SpForces = filteredlist;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView[] engageMethords = new ImageView[5];
        TextView forceId, forceName, forceDesc, forceTel, forceUrl, loadingTxt;
        RelativeLayout forceExtraLayout;
        CardView forceCard;
        String id;
        SpecificForce specificForce;
        boolean done;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            forceCard = itemView.findViewById(R.id.ForceCard);
            engageMethords[0] = itemView.findViewById(R.id.engage1);
            engageMethords[1] = itemView.findViewById(R.id.engage2);
            engageMethords[2] = itemView.findViewById(R.id.engage3);
            engageMethords[3] = itemView.findViewById(R.id.engage4);
            engageMethords[4] = itemView.findViewById(R.id.engage5);
            forceId = itemView.findViewById(R.id.ForceID);
            forceName = itemView.findViewById(R.id.ForceName);
            forceDesc = itemView.findViewById(R.id.ForceDesc);
            forceTel = itemView.findViewById(R.id.ForceTelephone);
            forceUrl = itemView.findViewById(R.id.ForceUrl);
            forceExtraLayout = itemView.findViewById(R.id.ForceExtra);
            forceExtraLayout.setVisibility(View.GONE);
            done = false;

            forceCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curpos = Integer.parseInt(v.getTag().toString());
                    if(detailsShowAt==curpos) {
                        detailsShowAt = -1;
                        notifyItemChanged(curpos);
                    } else{
                        int old = detailsShowAt;
                        detailsShowAt = curpos;
                        notifyItemChanged(old);
                        notifyItemChanged(detailsShowAt);
                    }
                }
            });
        }




         void setValues(SpecificForce force) {

            specificForce = force;

             id = specificForce.getId();
             forceId.setText(id);
             forceName.setText(specificForce.getName());

            if (specificForce.description != null)
                forceDesc.setText(Html.fromHtml(specificForce.description, Html.FROM_HTML_MODE_COMPACT));
            else forceDesc.setText("Not Available");
            if (specificForce.url != null) {
                forceUrl.setText(specificForce.url);
                forceUrl.setTag(specificForce.url);
                forceUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) v.getTag())));
                    }
                });
            } else forceUrl.setText("Not Available");
            forceTel.setText(specificForce.telephone);
            forceTel.setTag(specificForce.telephone);
            forceTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+v.getTag().toString())));
                }
            });

             if (specificForce.engagement_methods != null && !specificForce.engagement_methods.isEmpty() ) {
                 int e = 0;
                 for (Engagement_Methord em : specificForce.engagement_methods) {
                     Log.i(TAG, "onBindViewHolder: found engagement methord for "+id);
                     if (em.url != null && em.type!=null) {
                         switch (em.type) {
                             case "facebook":
                                 engageMethords[e].setVisibility(View.VISIBLE);
                                 engageMethords[e].setImageResource(R.mipmap.facebook);
                                 break;
                             case "twitter":
                                 engageMethords[e].setVisibility(View.VISIBLE);
                                 engageMethords[e].setImageResource(R.mipmap.twitter);
                                 break;
                             case "youtube":
                                 engageMethords[e].setVisibility(View.VISIBLE);
                                 engageMethords[e].setImageResource(R.mipmap.youtube);
                                 break;
                             case "flickr":
                                 engageMethords[e].setVisibility(View.VISIBLE);
                                 engageMethords[e].setImageResource(R.mipmap.flickr);
                                 break;
                             case "rss":
                                 engageMethords[e].setVisibility(View.VISIBLE);
                                 engageMethords[e].setImageResource(R.drawable.ic_web);
                                 break;
                         }
                         if(engageMethords[e].getVisibility() == View.VISIBLE) {
                             engageMethords[e].setTag(em.url);
                             engageMethords[e].setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Log.d(TAG, "engage methord onClick: " + v.getTag());
                                     mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString())));
                                 }
                             });
                             e++;
                         }
                     }
                 }
                 done = true;
             }

        }
    }
}
