package com.lahacks.fyp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
    Button btNotification;

    public static final int RESULT_ENABLE = 11;

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

        // cash in logic
        cashInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CashInPage.class);
                startActivity(intent);
            }
        });
    }


}