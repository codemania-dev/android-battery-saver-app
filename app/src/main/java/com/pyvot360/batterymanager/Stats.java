package com.pyvot360.batterymanager;

import static android.content.Context.USAGE_STATS_SERVICE;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class Stats extends Fragment {
    public Stats() {}

    String batteryPercentage;
    TextView batteryPercentageTextView, runningValueTextView, uptimeValueTextView;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;

            batteryPercentage = (int)batteryPct + "%";

            batteryPercentageTextView.setText(batteryPercentage);

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        batteryPercentageTextView = view.findViewById(R.id.battery_percentage_value_text_view);
        runningValueTextView = view.findViewById(R.id.running_value_text_view);
        uptimeValueTextView = view.findViewById(R.id.uptime_value_text_view);

        runningValueTextView.setText(String.valueOf(noRunningApps()));

        update(uptimeValueTextView,SystemClock.uptimeMillis());

        registerReceiver();

        return view;
    }

    private void registerReceiver() {
        getContext().registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    private int noRunningApps() {
        List<UsageStats> stats = null;
        // Process running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getContext().getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
        }
        return stats.size();
    }

    private String getFormattedVersion(Long lastTimeUsed) {
        @SuppressLint("DefaultLocale") String formattedTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(lastTimeUsed),
                TimeUnit.MILLISECONDS.toMinutes(lastTimeUsed) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(lastTimeUsed) % TimeUnit.MINUTES.toSeconds(1));
        return formattedTime;
    }


    // Trying to update the uptime every seconds. It hasn't been resolved.
    private void update(TextView view, long milliseconds){
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                view.setText(getFormattedVersion(milliseconds));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }


}
