package com.lahacks.fyp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class NotificationActivity extends BroadcastReceiver {

    @Override
    //constructs notification details
    public void onReceive(Context context, Intent intent){

        //this one is for five minutes
        NotificationCompat.Builder builder5 = new NotificationCompat.Builder(context, "notifyFiveMinute")
                .setSmallIcon(R.drawable.ic_time)
                .setContentTitle("Five minutes remaining")
                .setContentText("You only have five minutes remaining on the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager5 = NotificationManagerCompat.from(context);

        notificationManager5.notify(200, builder5.build());

        //this one is for one minute
        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(context, "notifyOneMinute")
                .setSmallIcon(R.drawable.ic_time)
                .setContentTitle("One Minute Remaining")
                .setContentText("You only have one minute remaining on the app")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(context);

        notificationManager1.notify(200, builder1.build());

        //this is one for when time is up
        NotificationCompat.Builder builder0 = new NotificationCompat.Builder(context, "timeUp")
                .setSmallIcon(R.drawable.ic_time)
                .setContentTitle("Time is up!")
                .setContentText("The app will now be blocked")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager0 = NotificationManagerCompat.from(context);

        notificationManager0.notify(200, builder0.build());


    }
}