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

public class MainActivity extends AppCompatActivity {

    private Button callBtn;
    private Button smsBtn;
    private Button mmsBtn;
    private Button webBtn;
    private Button geoBtn;

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

    private void addButtonHandlers(){
        callBtn = findViewById(R.id.callBtn);
        callBtn.setOnClickListener(buttonHandler(callBtn));

        smsBtn = findViewById(R.id.smsBtn);
        smsBtn.setOnClickListener(buttonHandler(smsBtn));

        mmsBtn = findViewById(R.id.mmsBtn);
        mmsBtn.setOnClickListener(buttonHandler(mmsBtn));

        webBtn = findViewById(R.id.webBtn);
        webBtn.setOnClickListener(buttonHandler(webBtn));

        geoBtn = findViewById(R.id.geoBtn);
        geoBtn.setOnClickListener(buttonHandler(geoBtn));
    }

    private View.OnClickListener buttonHandler(Button btn){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn.getId() == callBtn.getId()){
                    Intent call = new Intent();
                    call.setAction(Intent.ACTION_DIAL);
                    Uri phoneNumber = Uri.parse("tel:0102030405");
                    call.setData(phoneNumber);
                    try {
                        startActivity(call);
                    } catch (ActivityNotFoundException e){
                        Toast.makeText(MainActivity.this, R.string.error_app_unavailable, Toast.LENGTH_SHORT).show();
                    }
                } else if (btn.getId() == smsBtn.getId()){
                    Intent sms = new Intent();
                    sms.setAction(Intent.ACTION_SENDTO);
                    Uri phoneNumber = Uri.parse("sms:0102030405");
                    sms.setData(phoneNumber);
                    try {
                        startActivity(sms);
                    } catch (ActivityNotFoundException e){
                        Toast.makeText(MainActivity.this, R.string.error_app_unavailable, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
}