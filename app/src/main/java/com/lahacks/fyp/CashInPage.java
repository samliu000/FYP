package com.lahacks.fyp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private Button waterPopupButton;
    private Button prodPopupButton;
    private Button activityPopupButton;

    // Dialog components
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private int newMins; //This accumulates the minutes added by the user with each button
    private int newWater;
    private int newProd;
    private int newActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in_page);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        waterButton = findViewById(R.id.waterButton);
        prodButton = findViewById(R.id.prodButton);
        activityButton = findViewById(R.id.activityButton);
        submitButton = findViewById(R.id.submitButton); //newwww

        //NEW
        Intent cashOutIntent = new Intent(getApplicationContext(), CashOutPage.class);

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMins += addWaterMinutesDialog();
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        prodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Testing purposes
                Toast.makeText(CashInPage.this, "Productivity", Toast.LENGTH_SHORT).show();
                //adds 5 minutes to newMins - temporary until user input is coded
                newMins += addProdMinutesDialog();
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Testing purposes
                Toast.makeText(CashInPage.this, "Acitvity", Toast.LENGTH_SHORT).show();
                //adds 10 minutes to newMins - temporary until user input is coded
                newMins += addActivityMinutesDialog();
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

    public int addWaterMinutesDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View waterPopup = getLayoutInflater().inflate(R.layout.water_popup, null);
        dialogBuilder.setView(waterPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        waterPopupButton = (Button) waterPopup.findViewById(R.id.waterPopupButton);
        waterPopupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(CashInPage.this, "Cashed in water minutes!", Toast.LENGTH_SHORT).show();
                newWater = 1;
            }
        });
        return 1;
    }

    public int addProdMinutesDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View prodPopup = getLayoutInflater().inflate(R.layout.productivity_popup, null);
        dialogBuilder.setView(prodPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        prodPopupButton = (Button) prodPopup.findViewById(R.id.prodPopupButton);
        prodPopupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(CashInPage.this, "Cashed in productivity minutes!", Toast.LENGTH_SHORT).show();
                newProd = 30;
            }
        });
        return 30;
    }

    public int addActivityMinutesDialog(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View activityPopup = getLayoutInflater().inflate(R.layout.activity_popup, null);
        dialogBuilder.setView(activityPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        activityPopupButton = (Button) activityPopup.findViewById(R.id.activityPopupButton);
        activityPopupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(CashInPage.this, "Cashed in activity minutes!", Toast.LENGTH_SHORT).show();
                newActivity = 6;
            }
        });
        return 6;
    }
}