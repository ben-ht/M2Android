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
 * Vue permettant d'envoyer un message
 */
public class MessageActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText message;
    private String schema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        schema = intent.getStringExtra(IntentKeys.KEY_SCHEMA);
        phoneNumber = findViewById(R.id.numero);
        message = findViewById(R.id.message);

        Button validateBtn = findViewById(R.id.validate_button);
        validateBtn.setOnClickListener(openMessageApp());

        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(closeActivity());
    }

    /**
     * Valide la saisie d'un numéro de téléphone et d'un message
     * @return
     */
    private boolean validate(){
        return !phoneNumber.getText().toString().isEmpty()
                && !message.getText().toString().isEmpty();
    }

    /**
     * Ouvre une fenêtre d'envoi de message au destinataire et avec le contenu renseigné
     * @return
     */
    private View.OnClickListener openMessageApp(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()){
                    showError(MessageActivity.this, getString(R.string.numero_and_message_error_message));
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                Uri data = Uri.parse(schema + ":" + phoneNumber.getText().toString());
                intent.setData(data);
                intent.putExtra("sms_body", message.getText().toString());
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    appUnavailable(MessageActivity.this);
                }
            }
        };
    }

    /**
     * Retourne à la page d'accueil
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