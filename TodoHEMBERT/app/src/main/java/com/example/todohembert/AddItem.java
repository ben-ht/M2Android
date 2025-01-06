package com.example.todohembert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(addItem());

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(cancel());
    }

    /**
     * Retourne le nom de la tâche à l'activité principale
     * @return Nom de la tache
     */
    private View.OnClickListener addItem(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.addTaskInput);
                if (input.getText() != null && !input.getText().toString().isBlank()){
                    Intent intent = new Intent(AddItem.this, MainActivity.class);
                    intent.putExtra(MainActivity.KEY_TASK, input.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(AddItem.this,
                            "Veuillez saisir une tâche",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    /**
     * Retourne à la page principale sans ajouter de tâche
     */
    private View.OnClickListener cancel(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }
}