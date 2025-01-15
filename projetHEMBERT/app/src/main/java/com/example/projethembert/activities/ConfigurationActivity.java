package com.example.projethembert.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projethembert.R;

public class ConfigurationActivity extends AppCompatActivity {
    private RadioGroup difficultyGroup;
    private RadioButton customButton;

    private EditText playerHp;
    private EditText playerPower;
    private EditText monsterPower;

    private boolean isUserEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        difficultyGroup = findViewById(R.id.difficulty_group);
        customButton = findViewById(R.id.radio_custom);

        playerHp = findViewById(R.id.player_hp);
        playerPower = findViewById(R.id.player_power);
        monsterPower = findViewById(R.id.monster_power);

        setDefaultValues("Easy"); // Default to Easy difficulty

        difficultyGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (!isUserEditing) {
                if (checkedId == R.id.radio_easy) setDefaultValues("Easy");
                else if (checkedId == R.id.radio_medium) setDefaultValues("Medium");
                else if (checkedId == R.id.radio_hard) setDefaultValues("Hard");
                else clearFields(); // For custom, clear fields
            }
        });

        addTextWatcher(playerHp);
        addTextWatcher(playerPower);
        addTextWatcher(monsterPower);
    }

    private void setDefaultValues(String difficulty) {
        isUserEditing = true; // Prevent infinite loop
        if (difficulty.equals("Easy")) {
            playerHp.setText("100");
            playerPower.setText("10");
            monsterPower.setText("5");
        } else if (difficulty.equals("Medium")) {
            playerHp.setText("75");
            playerPower.setText("15");
            monsterPower.setText("10");
        } else if (difficulty.equals("Hard")) {
            playerHp.setText("50");
            playerPower.setText("20");
            monsterPower.setText("15");
        }
        isUserEditing = false;
    }

    private void clearFields() {
        isUserEditing = true;
        playerHp.setText("");
        playerPower.setText("");
        monsterPower.setText("");
        isUserEditing = false;
    }

    private void addTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUserEditing) {
                    customButton.setChecked(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
