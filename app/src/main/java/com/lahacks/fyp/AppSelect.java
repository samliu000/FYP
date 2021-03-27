package com.lahacks.fyp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lahacks.fyp.adapter.AppAdapter;
import com.lahacks.fyp.models.App;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppSelect extends AppCompatActivity {
    public static final String TAG = "AppSelect";

    // var declarations
    ImageView appIcon;
    List<App> allApps;
    Set<String> selectedApps;
    Button saveButton;
    String sample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);

        // var initializing
        allApps = new ArrayList<>();
        selectedApps = new HashSet<>();
        saveButton = findViewById(R.id.saveButton);

        RecyclerView rvApps = findViewById(R.id.rvApps);

        final AppAdapter appAdapter = new AppAdapter(this, allApps, selectedApps, sample);

        // attach adapter to recycler view
        rvApps.setAdapter(appAdapter);

        // set layout manager
        rvApps.setLayoutManager(new GridLayoutManager(this, 3));

        // add apps to the recyclerview
        allApps.addAll(getAllApplications());
        appAdapter.notifyDataSetChanged();

        // logic for the save Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                // compile list of package names of the selected apps
                ArrayList<String> selectedPackages = new ArrayList<>();
                for(App app: allApps) {
                    if(selectedApps.contains(app.getName())) {
                        selectedPackages.add(app.getPackageName());
                    }
                }

                // attach to intent and end activity
                intent.putExtra("selected", selectedPackages);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    /**
     * Get all the applications on the phone
     * @return list of all apps on the emulator
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