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
 * Vue permettant de visualiser une position sur une carte
 */
public class GeoActivity extends AppCompatActivity {

    private EditText latitude;
    private EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_geo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        Button validateBtn = findViewById(R.id.validate_button);
        validateBtn.setOnClickListener(openMap());

        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(closeActivity());
    }

    /**
     * Valide la saisie d'une latitude et d'une longitude
     * @return True si la saisie est valide, sinon False
     */
    private boolean validate(){
        return !latitude.getText().toString().isEmpty()
                && !longitude.getText().toString().isEmpty();
    }

    /**
     * Affiche une carte à la localisation renseignée
     */
    private View.OnClickListener openMap(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()){
                    showError(GeoActivity.this, getString(R.string.coordinates_error_message));
                    return;
                }

                Intent geo = new Intent();
                geo.setAction(Intent.ACTION_VIEW);
                Uri location = Uri.parse("geo:" + latitude.getText().toString() + ","
                        + longitude.getText().toString());
                geo.setData(location);
                try {
                    startActivity(geo);
                } catch (ActivityNotFoundException e){
                    appUnavailable(GeoActivity.this);
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