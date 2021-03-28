package com.lahacks.fyp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lahacks.fyp.models.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CashOutPage extends AppCompatActivity {
    public static final String TAG = "CashOutPage";
    public static final int SELECT = 20;

    // persistence
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private TextView text_view_countdown;
    private Button button_start_pause;
    private Button button_refresh;
    private Button chooseButton;
    private CountDownTimer countdown_timer;

    private long startTimeInMillis;
    private boolean timerRunning;
    private long timeLeftInMillis;
    private long endTime;
    private long millisLeft;
    private long oldMillisLeft;
    private long newMinsAdded;

    // app suspension declarations
    DevicePolicyManager dpm;
    DeviceAdminReceiver dar;
    private ComponentName compName;
    ImageView appSelection;
    public static final int RESULT_ENABLE = 11;
    String[] listOfPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out_page);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        // attaching views to timer stuff
        text_view_countdown = findViewById(R.id.text_view_countdown);
        button_start_pause = findViewById(R.id.button_start_pause);
        button_refresh = findViewById(R.id.button_refresh);

        // initialize device policy manager
        dpm = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        dar = new DeviceAdminReceiver();
        compName = new ComponentName(this, MyAdmin.class);

        // initialize buttons
        chooseButton = findViewById(R.id.button_choose);

        // persistence initialization
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        editor = prefs.edit();

        // apps to ban/unban
        String saved = prefs.getString("savedPackages", null);
        if(saved != null) {
            listOfPackages = saved.split("#");
        }
        else {
            listOfPackages = new String[1];
            listOfPackages[0] = "com.zhiliaoapp.musically";
        }


        // move to app selection
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CashOutPage.this, AppSelect.class);
                intent.putExtra("saved", listOfPackages);
                startActivityForResult(intent, SELECT);
            }
        });

        //starts or pauses the timer
        button_start_pause.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // kill or pause timer
                if(timerRunning) {
                    killApps();
                    pauseTimer();
                }
                // unkill or startTimer
                else {
                    unkillApps();
                    startTimer();
                    Toast.makeText(CashOutPage.this, "Reminder Set!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //this is for the adding new minutes logic
        Intent intent = getIntent();
        newMinsAdded = (long) intent.getIntExtra("newMins", 0);

        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //adds new minutes to the current timer
                if (newMinsAdded != 0) {
                    oldMillisLeft = millisLeft;
                    millisLeft = (newMinsAdded*60000) + oldMillisLeft;
                    newMinsAdded = 0;
                } else if (newMinsAdded == 0) {
                    Toast noMin = Toast.makeText(CashOutPage.this, "You have no minutes to cash out!", Toast.LENGTH_SHORT);
                    noMin.setGravity(Gravity.CENTER, 0, 0);
                    noMin.show();
                }

                setTime(millisLeft);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void killApps() {
        boolean active = dpm.isAdminActive(compName);

        // kill tik tok!
        if (active) {
            try {
                // suspend package
                dpm.setPackagesSuspended(compName, listOfPackages, true);
                Toast.makeText(CashOutPage.this, "Apps Successfully Blocked", Toast.LENGTH_SHORT).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void unkillApps() {
        boolean active = dpm.isAdminActive(compName);

        // unkill tik tok!
        if (active) {
            try {
                // suspend package
                dpm.setPackagesSuspended(compName, getAllPackageNames(), false);
                Toast.makeText(CashOutPage.this, "Apps Successfully Unblocked", Toast.LENGTH_SHORT).show();
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

    //setting time of timer seen by user...
    private void setTime(long milliseconds) {
        startTimeInMillis = milliseconds;
        resetTimer();
    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillis;

        countdown_timer = new CountDownTimer(timeLeftInMillis, 1000) {

            // if(millisLeft == 300000 || millisLeft == 60000) {send alarm}
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                millisLeft = timeLeftInMillis; // updating the start time every tick - this prevents users from getting more mins if they don't have new minutes added
                updateCountDownText();
            }

            // send notification if 0
            @Override
            public void onFinish() {
                timerRunning = false;
                millisLeft = 0; // sets millisLeft to 0, since time has ran out
                updateButtons();
            }
        }.start();

        timerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        countdown_timer.cancel();
        timerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        timeLeftInMillis = startTimeInMillis;
        updateCountDownText();
        updateButtons();
    }

    //updates button texts or hides buttons based on whether the timer is running
    private void updateButtons() {
        if(timerRunning) {
            button_start_pause.setText("Pause");
            button_refresh.setVisibility(View.INVISIBLE);
        } else {
            button_start_pause.setText("Start");
            button_refresh.setVisibility(View.VISIBLE);
            if(timeLeftInMillis < 1000) {
                button_start_pause.setVisibility(View.INVISIBLE);
            } else {
                button_start_pause.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateCountDownText() {
        int hours = (int) (timeLeftInMillis/1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        Intent intent = new Intent(CashOutPage.this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CashOutPage.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // this will send the create the appropriate notification
        if(minutes == 5){
            createNotificationChannel5();
            if(seconds == 5){
                long timeAtButtonClick = System.currentTimeMillis();
                //RTC_WAKEUP wakes up the device to fire the pending intent at the specified time
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick,
                        pendingIntent);
            }

        }

        if(minutes == 1){
            createNotificationChannel1();
            if(seconds==5){
                long timeAtButtonClick = System.currentTimeMillis();
                //RTC_WAKEUP wakes up the device to fire the pending intent at the specified time
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick,
                        pendingIntent);
            }
        }

        if(minutes == 0){
            createNotificationChannel0();
            if(seconds==5){
                long timeAtButtonClick = System.currentTimeMillis();
                //RTC_WAKEUP wakes up the device to fire the pending intent at the specified time
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        timeAtButtonClick,
                        pendingIntent);
            }
        }

        String timeLeftFormatted;
        if(hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
        text_view_countdown.setText(timeLeftFormatted);
    }

    //For configuration changes & when app is started/closed
    @Override
    protected void onStop() {
        super.onStop();

        editor.putLong("millisLeft", millisLeft); // saves millisLeft..
        editor.putLong("timeLeft", timeLeftInMillis);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTime);

        editor.apply();

        if (countdown_timer != null) {
            countdown_timer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        timeLeftInMillis = prefs.getLong("timeLeft", startTimeInMillis);
        millisLeft = prefs.getLong("millisLeft", timeLeftInMillis);
        timerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if (timerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMillis = endTime - System.currentTimeMillis();
            if (timeLeftInMillis < 0) {
                timeLeftInMillis = 0;
                timerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

    /**
     * Get all the applications on the phone
     * @return list of all apps on the emulator
     */
    public String[] getAllPackageNames() {
        final PackageManager pm = getPackageManager();

        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> allPackages = new ArrayList<>();

        // print out relevant info about each application
        for (ApplicationInfo packageInfo : packages) {

            // get package
            String pkg = packageInfo.packageName;

            // Only add packages that can be launched
            if(pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                if(!pkg.equals("com.lahacks.fyp")) {
                    allPackages.add(pkg);
                }
            }
        }

        return allPackages.toArray(new String[allPackages.size()]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT && resultCode == RESULT_OK) {
            Bundle args = data.getExtras();
            List<String> selectedPackages = (ArrayList<String>)args.getSerializable("selected");
            listOfPackages = selectedPackages.toArray(new String[selectedPackages.size()]);

            // persist data
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < listOfPackages.length; i++) {
                sb.append(listOfPackages[i]).append("#");
            }
            editor.putString("savedPackages", sb.toString());
            editor.commit();
        }
    }

    private void createNotificationChannel5(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "NotificationReminderChannel";
            String description = "Channel for Notification Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyFiveMinute", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationChannel1(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "NotificationReminderChannel";
            String description = "Channel for Notification Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyOneMinute", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    private void createNotificationChannel0(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "NotificationReminderChannel";
            String description = "Channel for Notification Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("timeUp", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}