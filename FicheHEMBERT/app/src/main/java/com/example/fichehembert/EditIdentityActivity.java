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
 * Vue permettant de renseigner l'identité
 */
public class EditIdentityActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_identity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editIdentity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setFields();
        addEventListeners();
        Intent data = getIntent();
        name.setText(data.getStringExtra(IntentKeys.NAME));
        surname.setText(data.getStringExtra(IntentKeys.SURNAME));
        phoneNumber.setText(data.getStringExtra(IntentKeys.PHONE_NUMBER));
    }

    /**
     * Ajoute les événements des clics sur les boutons
     */
    private void addEventListeners(){
        Button okBtn = findViewById(R.id.ok_button);
        okBtn.setOnClickListener(okHandler());

        Button cancelHandler = findViewById(R.id.cancel_button);
        cancelHandler.setOnClickListener(cancelHandler());
    }

    /**
     * Affecte les champs texte de l'interface à leur attribut
     */
    private void setFields() {
        name = findViewById(R.id.prenom);
        surname = findViewById(R.id.nom);
        phoneNumber = findViewById(R.id.telephone);
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
                result.putExtra(IntentKeys.NAME, name.getText().toString());
                result.putExtra(IntentKeys.SURNAME, surname.getText().toString());
                result.putExtra(IntentKeys.PHONE_NUMBER, phoneNumber.getText().toString());
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