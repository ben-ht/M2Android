package com.example.fichehembert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Vue de la fiche de contact
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Callback du formulaire d'identité
     * Récupère les résultats et les affiches sur la fiche de contact
     */
    private final ActivityResultLauncher<Intent> getIdentityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent response = result.getData();
                        if (response == null)
                            return;

                        name.setText(response.getStringExtra(IntentKeys.NAME));
                        surname.setText(response.getStringExtra(IntentKeys.SURNAME));
                        phoneNumber.setText(response.getStringExtra(IntentKeys.PHONE_NUMBER));
                    }
                }
            }
    );

    /**
     * Callback du formulaire d'adresse
     * Récupère les résultats et les affiches sur la fiche de contact
     */
    private final ActivityResultLauncher<Intent> getAddressResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent response = result.getData();
                        if (response == null)
                            return;

                        number.setText(response.getStringExtra(IntentKeys.NUMBER));
                        road.setText(response.getStringExtra(IntentKeys.ROAD));
                        postalCode.setText(response.getStringExtra(IntentKeys.POSTAL_CODE));
                        city.setText(response.getStringExtra(IntentKeys.CITY));
                    }
                }
            }
    );

    private TextView name;
    private TextView surname;
    private TextView phoneNumber;
    private TextView number;
    private TextView road;
    private TextView postalCode;
    private TextView city;

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

        setFields();
        addEventListeners();
    }

    /**
     * Ajoute les événements des clics sur les boutons
     */
    private void addEventListeners(){
        Button editIdentityBtn = findViewById(R.id.editIdentity);
        editIdentityBtn.setOnClickListener(editIdentityHandler());

        Button editAddressBtn = findViewById(R.id.editAddress);
        editAddressBtn.setOnClickListener(editAddressHandler());
    }

    /**
     * Affecte les champs texte de l'interface à leur attribut
     */
    private void setFields(){
        name = findViewById(R.id.prenom);
        surname = findViewById(R.id.nom);
        phoneNumber = findViewById(R.id.telephone);
        number = findViewById(R.id.numero);
        road = findViewById(R.id.rue);
        postalCode = findViewById(R.id.code_postal);
        city = findViewById(R.id.ville);
    }

    /**
     * Définit le comportement du bouton Modifier l'identité
     * Redirige sur le formulaire de saisie d'identité en préremplissant les champs
     */
    private View.OnClickListener editIdentityHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(MainActivity.this, EditIdentityActivity.class);
                data.putExtra(IntentKeys.NAME, name.getText().toString());
                data.putExtra(IntentKeys.SURNAME, surname.getText().toString());
                data.putExtra(IntentKeys.PHONE_NUMBER, phoneNumber.getText().toString());
                getIdentityResult.launch(data);
            }
        };
    }

    /**
     * Définit le comportement du bouton Modifier l'adresse
     * Redirige sur le formulaire de saisie d'adresse en préremplissant les champs
     */
    private View.OnClickListener editAddressHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent(MainActivity.this, EditAddressActivity.class);
                data.putExtra(IntentKeys.NUMBER, number.getText().toString());
                data.putExtra(IntentKeys.ROAD, road.getText().toString());
                data.putExtra(IntentKeys.POSTAL_CODE, postalCode.getText().toString());
                data.putExtra(IntentKeys.CITY, city.getText().toString());
                getAddressResult.launch(data);
            }
        };
    }
}