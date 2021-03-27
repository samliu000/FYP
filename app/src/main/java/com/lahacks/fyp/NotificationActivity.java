package com.lahacks.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NotificationActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        //constructs notification details
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyLemubit")
                .setSmallIcon(R.drawable.ic_time)
                .setContentTitle("Five minutes remaining")
                .setContentText("You only have five minutes remaining on the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager5 = NotificationManagerCompat.from(context);

        notificationManager5.notify(200, builder.build());
    }
}