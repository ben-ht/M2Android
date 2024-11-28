package com.example.tp4hembert;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Vue principale
 */
public class MainActivity extends AppCompatActivity {

    private final static String SMS = "sms";
    private final static String MMS = "mms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addButtonHandlers();
    }

    /**
     * Ajoute à chaque bouton son événement onClick
     */
    private void addButtonHandlers(){
        Button callBtn = findViewById(R.id.callBtn);
        callBtn.setOnClickListener(callButtonHandler());

        Button smsBtn = findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(messageButtonHandler(SMS));

        Button mmsBtn = findViewById(R.id.mmsBtn);
        mmsBtn.setOnClickListener(messageButtonHandler(MMS));

        Button webBtn = findViewById(R.id.webBtn);
        webBtn.setOnClickListener(webButtonHandler());

        Button geoBtn = findViewById(R.id.geoBtn);
        geoBtn.setOnClickListener(geoButtonHandler());
    }

    /**
     * Ouvre la page d'appel
     */
    private View.OnClickListener callButtonHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CallActivity.class));
            }
        };
    }

    /**
     * Ouvre la page de message
     * @param schema Schema de l'intention (sms ou mms)
     */
    private View.OnClickListener messageButtonHandler(String schema){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent message = new Intent(MainActivity.this, MessageActivity.class);
                message.putExtra(IntentKeys.KEY_SCHEMA, schema);
                startActivity(message);
            }
        };
    }

    /**
     * Ouvre la page permettant d'ouvrir un navigateur
     */
    private View.OnClickListener webButtonHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebActivity.class));
            }
        };
    }

    /**
     * Ouvre la page permettant d'ouvrir une carte à une position
     * @return
     */
    private View.OnClickListener geoButtonHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GeoActivity.class));
            }
        };
    }
}