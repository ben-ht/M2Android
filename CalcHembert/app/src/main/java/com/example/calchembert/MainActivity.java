package com.example.calchembert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private final String _notANumber = "NaN";

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

        EditText value1 = findViewById(R.id.value1);
        EditText value2 = findViewById(R.id.value2);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button razButton = findViewById(R.id.raz);
        Button equalsButton = findViewById(R.id.equals);
        Button leaveButton = findViewById(R.id.leave);
        TextView result = findViewById(R.id.result);

        razButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value1.setText("");
                value2.setText("");
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        equalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int checkedRadioId = radioGroup.getCheckedRadioButtonId();
                    if (checkedRadioId == -1) {
                        CharSequence text = "Sélectionnez un opérateur";
                        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    RadioButton radio = findViewById(checkedRadioId);
                    String radioText = radio.getText().toString();
                    double val1 = Integer.parseInt(value1.getText().toString());
                    double val2 = Integer.parseInt(value2.getText().toString());

                    if (value1.getText() == null || value2.getText() == null) {
                        CharSequence text = "Complétez les 2 opérandes";
                        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
                        toast.show();
                    } else if ((val1 == 0 || val2 == 0) && radioText.equals("divisé")){
                        result.setText("Erreur - division par 0");
                    }

                    double operation = 0;
                        switch (radioText) {
                            case "plus":
                                operation = val1 + val2;
                                break;
                            case "moins":
                                operation = val1 - val2;
                                break;
                            case "multiplié":
                                operation = val1 * val2;
                                break;
                            case "divisé":
                                operation = val1/val2;
                                break;
                        }
                    result.setText(String.valueOf(operation));

                } catch (Exception ex) {
                     result.setText(_notANumber);
                }
            }
        });
    }
}