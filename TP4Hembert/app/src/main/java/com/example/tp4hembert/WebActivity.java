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
 * Vue permettant d'ouvrir un navigateur
 */
public class WebActivity extends AppCompatActivity {

    private EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        url = findViewById(R.id.url);

        Button validateBtn = findViewById(R.id.validate_button);
        validateBtn.setOnClickListener(openBrowser());

        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(closeActivity());
    }

    /**
     * Valide la saisie de l'URL
     * @return True si la saisie est valide, sinon False
     */
    private boolean validate(){
        return !url.getText().toString().isEmpty();
    }

    /**
     * Ouvre un navigateur à la page renseignée si la saisie est valide
     */
    private View.OnClickListener openBrowser(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()){
                    showError(WebActivity.this, getString(R.string.url_error_message));
                    return;
                }

                Intent web = new Intent();
                web.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url.getText().toString());
                web.setData(uri);
                try {
                    startActivity(web);
                } catch (ActivityNotFoundException e){
                    appUnavailable(WebActivity.this);
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