package com.example.tp4hembert;

import static com.example.tp4hembert.ToastHelper.appUnavailable;
import static com.example.tp4hembert.ToastHelper.showError;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Vue de la page permettante d'appeler
 */
public class CallActivity extends AppCompatActivity {

    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_call);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        phoneNumber = findViewById(R.id.numero);

        Button validateBtn = findViewById(R.id.validate_button);
        validateBtn.setOnClickListener(openCallApplication());

        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(closeActivity());
    }

    /**
     * Valide la saisie du numéro de téléphone
     * @return True si la saisie est valide, sinon False
     */
    private boolean validate(){
        return !phoneNumber.getText().toString().isEmpty();
    }

    /**
     * Ouvre une application permettant de passer un appel si la saisie est valide
     */
    private View.OnClickListener openCallApplication(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    showError(CallActivity.this, getString(R.string.numero_error_message));
                    return;
                }

                Intent call = new Intent();
                call.setAction(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNumber.getText().toString());
                call.setData(data);
                try {
                    startActivity(call);
                } catch (ActivityNotFoundException e){
                    appUnavailable(CallActivity.this);
                }
            }
        };
    }

    /**
     * Retourne à la page principale
     */
    private View.OnClickListener closeActivity(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }

}