package com.lahacks.fyp;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProdPopup extends AppCompatActivity {

    private Button prodPopupButton;
    private long addedMins;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productivity_popup);

        Intent intent = getIntent();
        addedMins = (long) intent.getIntExtra("newMins", 0);

        prodPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedMins += 6;
                intent.putExtra("newMins", addedMins);
            }
        });
    }
}


