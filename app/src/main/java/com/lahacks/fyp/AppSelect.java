package com.lahacks.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.lahacks.fyp.adapter.AppAdapter;
import com.lahacks.fyp.models.App;

import java.util.ArrayList;
import java.util.List;

public class AppSelect extends AppCompatActivity {
    public static final String TAG = "AppSelect";
    ImageView appIcon;
    List<App> allApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);

        allApps = new ArrayList<>();

        RecyclerView rvApps = findViewById(R.id.rvApps);

        final AppAdapter appAdapter = new AppAdapter(this, allApps);

        // attach adapter to recycler view
        rvApps.setAdapter(appAdapter);

        // set layout manager
        rvApps.setLayoutManager(new GridLayoutManager(this, 3));

        allApps.addAll(getAllApplications());
        appAdapter.notifyDataSetChanged();

//        appIcon = findViewById(R.id.appIcon);
//        try {
//            String pkg = "com.zhiliaoapp.musically";//your package name
//            Drawable icon = this.getPackageManager().getApplicationIcon(pkg);
//            this.getPackageManager().getApplicationLabel(pkg);
//            appIcon.setImageDrawable(icon);
//        } catch (PackageManager.NameNotFoundException ne) {
//
//        }
    }

    /**
     * Get all the applications on the phone
     * @return
     */
    public List<App> getAllApplications() {
        final PackageManager pm = getPackageManager();

        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<App> allApps = new ArrayList<>();

        // print out relevant info about each application
        for (ApplicationInfo packageInfo : packages) {


            // get package, app name, and icon
            String pkg = packageInfo.packageName;
            String appName = this.getPackageManager().getApplicationLabel(packageInfo).toString();
            Drawable icon;
            try {
                icon = this.getPackageManager().getApplicationIcon(pkg);

            } catch (PackageManager.NameNotFoundException ne) {
                Log.e(TAG, "Error with getting icon: " + ne);
                icon = null;
            }

            // create app model based on data
            App app = new App(appName, pkg, icon);

            // Only add packages that can be launched
            if(pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
//                Log.d(TAG, "Installed package :" + packageInfo.packageName);
//                Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//                Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                if(!pkg.equals("com.lahacks.fyp")) {
                    allApps.add(app);
                }
            }
        }
        return allApps;
    }

}