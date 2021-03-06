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
    private static int newWater;
    private int newProd;
    private int newActivity;

    Intent cashOutIntent;

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
        newWater = 0;

        //NEW
        cashOutIntent = new Intent(getApplicationContext(), CashOutPage.class);

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWaterMinutesDialog();
            }
        });

        prodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProdMinutesDialog();
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityMinutesDialog();
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

    public void addWaterMinutesDialog() {

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
                newMins += 1;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });
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
                newMins += 30;
                cashOutIntent.putExtra("newMins", newMins);
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
                newMins += 6;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });
        return 6;
    }
}