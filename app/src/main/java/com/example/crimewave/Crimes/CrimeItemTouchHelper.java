package com.example.crimewave.Crimes;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class CrimeItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private CrimeItemTouchHelperListner listner;
    private boolean isLatLng;

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder !=null){
            final View foreground = ((CrimeRecyclerAdapter.CrimeViewHolder) viewHolder).CrimeForeGround;

            getDefaultUIUtil().onSelected(foreground);
        }
    }

    public CrimeItemTouchHelper(int dragDirs, int swipeDirs, CrimeItemTouchHelperListner listner,@Nullable Boolean isLatLng) {
        super(dragDirs, swipeDirs);
        this.listner = listner;
        if(isLatLng!=null)this.isLatLng = isLatLng;
    }



    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        listner.onSwiped(viewHolder,i,viewHolder.getAdapterPosition(),isLatLng);

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foreground = ((CrimeRecyclerAdapter.CrimeViewHolder) viewHolder).CrimeForeGround;

        getDefaultUIUtil().onDraw(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        final View foreground = ((CrimeRecyclerAdapter.CrimeViewHolder) viewHolder).CrimeForeGround;
        getDefaultUIUtil().onDrawOver(c,recyclerView,foreground,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foreground = ((CrimeRecyclerAdapter.CrimeViewHolder) viewHolder).CrimeForeGround;
        getDefaultUIUtil().clearView(foreground);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface CrimeItemTouchHelperListner {
        void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int position,@Nullable Boolean isLatLng);
    }
}
