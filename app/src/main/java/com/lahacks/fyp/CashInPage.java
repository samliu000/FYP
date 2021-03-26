package com.lahacks.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CashInPage extends AppCompatActivity {

    private Button waterButton;
    private Button prodButton;
    private Button activityButton;
    private Button submitButton;

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
                //Testing purposes
                Toast.makeText(CashInPage.this, "Water", Toast.LENGTH_SHORT).show();
                //adds 1 minute to newMins - temporary until user input is coded
                newMins += 1;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        prodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Testing purposes
                Toast.makeText(CashInPage.this, "Productivity", Toast.LENGTH_SHORT).show();
                //adds 5 minutes to newMins - temporary until user input is coded
                newMins += 5;
                cashOutIntent.putExtra("newMins", newMins);
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Testing purposes
                Toast.makeText(CashInPage.this, "Acitvity", Toast.LENGTH_SHORT).show();
                //adds 10 minutes to newMins - temporary until user input is coded
                newMins += 10;
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
}