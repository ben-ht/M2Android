package com.example.fichehembert;

import android.content.Intent;
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
 * Vue permettant de renseigner l'adresse
 */
public class EditAddressActivity extends AppCompatActivity {

    private EditText number;
    private EditText road;
    private EditText postalCode;
    private EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editAddress), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setFields();
        addEventListeners();

        Intent data = getIntent();
        number.setText(data.getStringExtra(IntentKeys.NUMBER));
        road.setText(data.getStringExtra(IntentKeys.ROAD));
        postalCode.setText(data.getStringExtra(IntentKeys.POSTAL_CODE));
        city.setText(data.getStringExtra(IntentKeys.CITY));
    }

    /**
     * Ajoute les événements des clics sur les boutons
     */
    private void addEventListeners() {
        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(okHandler());

        Button cancelHandler = findViewById(R.id.cancel_button);
        cancelHandler.setOnClickListener(cancelHandler());
    }

    /**
     * Affecte les champs texte de l'interface à leur attribut
     */
    private void setFields() {
        number = findViewById(R.id.numero);
        road = findViewById(R.id.rue);
        postalCode = findViewById(R.id.code_postal);
        city = findViewById(R.id.ville);
    }

    /**
     * Définit le comportement du bouton Ok
     * Retourne les valeurs des champs texte à la vue principale
     * @return Intention contenant les valeurs
     */
    private View.OnClickListener okHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(IntentKeys.NUMBER, number.getText().toString());
                result.putExtra(IntentKeys.ROAD, road.getText().toString());
                result.putExtra(IntentKeys.POSTAL_CODE, postalCode.getText().toString());
                result.putExtra(IntentKeys.CITY, city.getText().toString());
                setResult(RESULT_OK, result);
                finish();
            }
        };
    }

    /**
     * Définit le comportement du bouton Annuler
     * Retourne sur la vue principale sans tenir compte des modifications
     */
    private View.OnClickListener cancelHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }
}