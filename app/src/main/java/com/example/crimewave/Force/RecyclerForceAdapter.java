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

    private ArrayList<ForcesBasic> Forces;
    private Context mContext;
    private int detailsShowAt = -1;
    private FragmentActivity activity;

    public RecyclerForceAdapter(Context context, @Nullable ArrayList<ForcesBasic> forces, FragmentActivity activity) {
        this.activity = activity;
        Log.d(TAG, "RecyclerForceAdapter: constructr called with forces" + forces);
        Forces = forces;
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
        viewHolder.id = Forces.get(i).id;
        viewHolder.forceId.setText(viewHolder.id);
        viewHolder.forceName.setText(Forces.get(i).name);
        viewHolder.forceCard.setTag(i);
        if(detailsShowAt==i)viewHolder.expand();
        else viewHolder.forceExtraLayout.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        int a;
        if (Forces == null) a = 0;
        else if (Forces.isEmpty()) a = 0;
        else a = Forces.size();
        return a;
    }

    public void showlist(ArrayList<ForcesBasic> filteredlist) {
        Forces = filteredlist;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout EngageMethordLayout;
        TextView forceId, forceName, forceDesc, forceTel, forceUrl, loadingTxt;
        RelativeLayout forceExtraLayout;
        CardView forceCard;
        SpecificForce specificForce;
        String id;
        boolean isexpanded = false;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            forceCard = itemView.findViewById(R.id.ForceCard);
            EngageMethordLayout = itemView.findViewById(R.id.ForceEngages);
            forceId = itemView.findViewById(R.id.ForceID);
            forceName = itemView.findViewById(R.id.ForceName);
            forceDesc = itemView.findViewById(R.id.ForceDesc);
            forceTel = itemView.findViewById(R.id.ForceTelephone);
            forceUrl = itemView.findViewById(R.id.ForceUrl);
            forceExtraLayout = itemView.findViewById(R.id.ForceExtra);
            forceExtraLayout.setVisibility(View.GONE);
            loadingTxt = itemView.findViewById(R.id.loadingtxt);

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

         private void setSpecificForce() {
            if (specificForce == null) {
                new GetForceAdapter().getSpecificForceCall(id).enqueue(new Callback<SpecificForce>() {
                    @Override
                    public void onResponse(Call<SpecificForce> call, Response<SpecificForce> response) {
                        if(response.body()!=null) {
                            specificForce = response.body();
                            setExtras();
                            if(specificForce.id.equals(id)) {
                                forceExtraLayout.setVisibility(View.VISIBLE);
                                Log.i(TAG, "onResponse: "+specificForce.engagement_methods);
                                isexpanded = true;
                                loadingTxt.setVisibility(View.GONE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<SpecificForce> call, Throwable t) {
                        Toast.makeText(mContext,"cannot connect to server!!!",Toast.LENGTH_LONG).show();
                        loadingTxt.setVisibility(View.GONE);
                        Snackbar.make(activity.findViewById(android.R.id.content),"Please check your connection",Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setSpecificForce();
                                    }
                                })
                                .show();
                        Log.e(TAG, "onFailure: ", t);
                    }
                });

            }
        }

        void expand(){
            if(specificForce==null)setSpecificForce();
            else forceExtraLayout.setVisibility(View.VISIBLE);
        }

         void setExtras() {

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
            if (specificForce.engagement_methods != null && !specificForce.engagement_methods.isEmpty()) {
                for (Engagement_Methord em : specificForce.engagement_methods) {
                    Log.i(TAG, "onBindViewHolder: found engagement methord");
                    if (em.url != null && em.type!=null) {
                        ImageView engageicon = new ImageView(mContext);
                        LinearLayout.LayoutParams engageparams = new LinearLayout.LayoutParams(70,70);
                        engageparams.setMarginEnd(40);
                        engageicon.setLayoutParams(engageparams);
                        switch (em.type) {
                            case "facebook":
                                engageicon.setImageResource(R.mipmap.facebook);
                                break;
                            case "twitter":
                                engageicon.setImageResource(R.mipmap.twitter);
                                break;
                            case "youtube":
                                engageicon.setImageResource(R.mipmap.youtube);
                                break;
                            case "flickr":
                                engageicon.setImageResource(R.mipmap.flickr);
                                break;
                            case "rss":
                                engageicon.setImageResource(R.drawable.ic_web);
                                break;
                            default:
                                continue;
                        }
                        engageicon.setTag(em.url);
                        engageicon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString())));
                            }
                        });
                        EngageMethordLayout.addView(engageicon);
                    }
                }
            }
        }
    }
}
