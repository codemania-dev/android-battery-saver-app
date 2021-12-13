package com.pyvot360.batterymanager;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.USAGE_STATS_SERVICE;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

// How far Jerry? Make sure you make this app have usage access your phone settings.

public class Running extends Fragment {
    public Running() {}

    RecyclerView recyclerView;
    ArrayList<String> appNames = new ArrayList<>();
    ArrayList<Drawable> appIcons = new ArrayList<Drawable>();
    ArrayList<Long> appTimeStamps = new ArrayList<>();

    PackageManager pm;

    RunningAppRecyclerAdapter runningAppRecyclerAdapter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running, container, false);
        Log.i("Running", "In onCreateView() method");

        pm = getContext().getPackageManager();

        recyclerView = view.findViewById(R.id.runningAppRecyclerView);


        getAllRunningApps(getAllApps());

        runningAppRecyclerAdapter = new RunningAppRecyclerAdapter(appNames, appIcons, appTimeStamps);

        recyclerView.setAdapter(runningAppRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private ArrayList<ApplicationInfo> getInstalledApps(){
        ArrayList<ApplicationInfo> listOfInstalledApps = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        List<ApplicationInfo> applications = pm.getInstalledApplications(flags);

        for (ApplicationInfo appInfo : applications) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                // System application
            } else {
                listOfInstalledApps.add(appInfo);
            }
        }

        return listOfInstalledApps;

    }

    private ArrayList<ApplicationInfo> getSystemApps(){
        ArrayList<ApplicationInfo> listOfSystemApps = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        List<ApplicationInfo> applications = pm.getInstalledApplications(flags);

        for (ApplicationInfo appInfo : applications) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                // System application
                listOfSystemApps.add(appInfo);
            } else {

            }
        }

        return listOfSystemApps;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<ApplicationInfo> getAllApps(){

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        pm = getContext().getPackageManager();
        ArrayList<ApplicationInfo> applications = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(flags);


        return applications;
    }

    private void getAllRunningApps(ArrayList<ApplicationInfo> apps) {
        // Process running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getContext().getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);

            // Sort the stats by the last time used
            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);

                    String backgroundAppPackageName = usageStats.getPackageName();
                        String backgroundAppName = getAppName(backgroundAppPackageName, apps);
                        Drawable backgroundAppIcon = getAppIcon(backgroundAppPackageName, apps);
                        long appTimeUsed = usageStats.getLastTimeUsed();


                        appNames.add(backgroundAppName);
                        appIcons.add(backgroundAppIcon);
                        appTimeStamps.add(appTimeUsed);

                }


            }
        }
    }

    private Drawable getAppIcon(String backgroundAppPackageName, ArrayList<ApplicationInfo> apps) {
        Drawable backgroundAppIcon = null;
        for (ApplicationInfo eachApp: apps){
            String eachInstalledAppPackageName = eachApp.packageName;
            if (eachInstalledAppPackageName.equals(backgroundAppPackageName)){
                backgroundAppIcon = pm.getApplicationIcon(eachApp); //Icon //Package label(app name)
                break;
            }
        }
        return backgroundAppIcon;
    }

    private String getAppName(String backgroundAppPackageName, ArrayList<ApplicationInfo> listOfInstalledApps) {
        String backgroundAppName = "";
        for (ApplicationInfo eachApp: listOfInstalledApps){
            String eachInstalledAppPackageName = eachApp.packageName;
            if (eachInstalledAppPackageName.equals(backgroundAppPackageName)){
                backgroundAppName = String.valueOf(pm.getApplicationLabel(eachApp)); //Package label(app name)
                break;
            }
        }
        return backgroundAppName;
    }


}