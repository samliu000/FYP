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
    public static final String TAG = "MainActivity";

    Button cashOutButton;
    Button cashInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // attaching views
        cashOutButton = findViewById(R.id.cashOutButton);
        cashInButton = findViewById(R.id.cashInButton);

        // cash out logic
        cashOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CashOutPage.class);
                startActivity(intent);
            }
        });

        // cash out logic
        cashInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CashInPage.class);
                startActivity(intent);
            }
        });

    }
}