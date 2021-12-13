package com.pyvot360.batterymanager;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RunningAppRecyclerAdapter extends RecyclerView.Adapter<RunningAppRecyclerAdapter.ViewHolder>{
    ArrayList<String> appNames;
    ArrayList<Drawable> appIcons;
    ArrayList<Long> appTimeStamps;

    public RunningAppRecyclerAdapter(ArrayList<String> appNames, ArrayList<Drawable> appIcons, ArrayList<Long> appTimeStamps) {
        this.appNames = appNames;
        this.appIcons = appIcons;
        this.appTimeStamps = appTimeStamps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_running_app_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("RunningRecyclerAdapter", "***************************************************************************");
        Log.i("RunningRecyclerAdapter", "onBindViewHolder() method");
        Log.i("RunningRecyclerAdapter", "***************************************************************************");
        holder.appIcon.setImageDrawable(appIcons.get(position));
        holder.appName.setText(appNames.get(position));
        holder.appTimeUsed.setText(getFormattedVersion(appTimeStamps.get(position)));


    }

    private String getFormattedVersion(Long lastTimeUsed) {
        @SuppressLint("DefaultLocale") String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(lastTimeUsed),
                TimeUnit.MILLISECONDS.toMinutes(lastTimeUsed) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(lastTimeUsed) % TimeUnit.MINUTES.toSeconds(1));
        return formattedTime;
    }

    @Override
    public int getItemCount() {
        return appNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView appIcon;
        TextView appName;
        TextView appTimeUsed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            appTimeUsed = itemView.findViewById(R.id.appTimeUsed);
        }
    }


}
