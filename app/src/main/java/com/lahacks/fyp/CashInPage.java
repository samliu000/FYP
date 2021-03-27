package com.lahacks.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CashInPage extends AppCompatActivity {

    // Declare buttons
    private Button waterButton;
    private Button prodButton;
    private Button activityButton;
    private Button submitButton;

    // Dialog components
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private int newMins; //This accumulates the minutes added by the user with each button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in_page);

        waterButton = findViewById(R.id.waterButton);
        prodButton = findViewById(R.id.prodButton);
        activityButton = findViewById(R.id.activityButton);
        submitButton = findViewById(R.id.submitButton); //newwww

        //NEW
        Intent cashOutIntent = new Intent(getApplicationContext(), CashOutPage.class);

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWaterMinutesDialog();
                //correct minutes (1 tiktok minute/1 cup water)
                newMins += 1;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        prodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProdMinutesDialog();
                //correct minutes (30 tiktok minutes/1 hour productivity)
                newMins += 30;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityMinutesDialog();
                //correct minutes (6 tiktok minutes/30 minutes activity)
                newMins += 6;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        // Takes the user back to cashOutPage (timer) and records the new minutes added
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(cashOutIntent);
                // Resets everything once the new minutes is added to timer...
                newMins = 0;
                cashOutIntent.putExtra("newMins", 0);
            }
        });
    }

    public void addWaterMinutesDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View waterPopup = getLayoutInflater().inflate(R.layout.water_popup, null);
        dialogBuilder.setView(waterPopup);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void addProdMinutesDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View prodPopup = getLayoutInflater().inflate(R.layout.productivity_popup, null);
        dialogBuilder.setView(prodPopup);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void addActivityMinutesDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View activityPopup = getLayoutInflater().inflate(R.layout.activity_popup, null);
        dialogBuilder.setView(activityPopup);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}