package com.lahacks.fyp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    public static final int RESULT_ENABLE = 11;
    public static final String TAG = "MainActivity";
    DevicePolicyManager dpm;
    DeviceAdminReceiver dar;
    Button killButton;
    Button unkillButton;
    Button mysteryButton;
    ImageView appSelection;
    private ComponentName compName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize device policy manager
        dpm = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dar = new DeviceAdminReceiver();
        compName = new ComponentName(this, MyAdmin.class);

        // initialize buttons
        killButton = findViewById(R.id.killButton);
        unkillButton = findViewById(R.id.unkillButton);
        mysteryButton = findViewById(R.id.mysteryButton);
        appSelection = findViewById(R.id.appSelection);

        // apps to ban/unban
        String[] listOfPackages = {"com.google.android.youtube", "com.zhiliaoapp.musically"};

        // trap
        mysteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You fool! It does nothing", Toast.LENGTH_SHORT).show();
            }
        });

        // move to app selection
        appSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppSelect.class);
                startActivity(intent);
            }
        });

        // logic for the killButton
        killButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                boolean active = dpm.isAdminActive(compName);

                // kill tik tok!
                if (active) {
                    try {
                        // suspend package
                        dpm.setPackagesSuspended(compName, listOfPackages, true);
                        Toast.makeText(MainActivity.this, "Apps Successfully Blocked", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d(TAG, "Security Exception " + e);
                    }
                }

                // ask for admin privilege
                else {

                    // create intent to go to page where you ask for admin permission
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "We need " +
                            "this permission to block apps");
                    startActivityForResult(intent, RESULT_ENABLE);
                }
            }
        });

        unkillButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                boolean active = dpm.isAdminActive(compName);

                // unkill tik tok!
                if (active) {
                    try {
                        // suspend package
                        dpm.setPackagesSuspended(compName, listOfPackages, false);
                        Toast.makeText(MainActivity.this, "Apps Successfully Unblocked", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d(TAG, "Security Exception " + e);
                    }
                }

                // ask for admin privilege
                else {

                    // create intent to go to page where you ask for admin permission
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "We need " +
                            "this permission to block apps");
                    startActivityForResult(intent, RESULT_ENABLE);
                }
            }
        });
    }
}