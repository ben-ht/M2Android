package com.example.projethembert.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projethembert.R;
import com.example.projethembert.entities.enums.Difficulty;
import com.example.projethembert.utils.Config;
import com.example.projethembert.utils.IntentKeys;

public class ConfigurationActivity extends AppCompatActivity {
    private RadioGroup difficultyGroup;
    private RadioButton customButton;
    private EditText playerHp;
    private EditText playerPower;
    private EditText monsterPower;
    private Button startGame;
    private Config config;

    private boolean isUserEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Intent intent = getIntent();
        config = intent.getParcelableExtra(IntentKeys.CONFIG);

        difficultyGroup = findViewById(R.id.difficulty_group);
        customButton = findViewById(R.id.radio_custom);

        startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(startGame());

        playerHp = findViewById(R.id.player_hp);
        playerPower = findViewById(R.id.player_power);
        monsterPower = findViewById(R.id.monster_power);


        difficultyGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (!isUserEditing) {
                if (checkedId == R.id.radio_easy) setDefaultValues(Difficulty.EASY);
                else if (checkedId == R.id.radio_medium) setDefaultValues(Difficulty.MEDIUM);
                else if (checkedId == R.id.radio_hard) setDefaultValues(Difficulty.HARD);
                else setDefaultValues(Difficulty.CUSTOM);
            }
        });

        addTextWatcher(playerHp);
        addTextWatcher(playerPower);
        addTextWatcher(monsterPower);

        setDefaultValues(config.getDifficulty());
        switch (config.getDifficulty()) {
            case EASY:
                ((RadioButton)findViewById(R.id.radio_easy)).setChecked(true);
                break;
            case MEDIUM:
                ((RadioButton)findViewById(R.id.radio_medium)).setChecked(true);
                break;
            case HARD:
                ((RadioButton)findViewById(R.id.radio_hard)).setChecked(true);
                break;
            default:
                customButton.setChecked(true);
        }
    }

    private void setDefaultValues(Difficulty difficulty) {
        config.setDifficulty(difficulty);
        if (difficulty != Difficulty.CUSTOM) {
            isUserEditing = true;
            playerHp.setText(String.valueOf(difficulty.getPlayerHealth()));
            playerPower.setText(String.valueOf(difficulty.getPlayerPower()));
            monsterPower.setText(String.valueOf(difficulty.getMaxMonsterPower()));
            isUserEditing = false;
        }
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

    private View.OnClickListener startGame(){
        if (config.getDifficulty() == Difficulty.CUSTOM){
            config.getDifficulty().setPlayerHealth(Integer.parseInt(playerHp.getText().toString()));
            config.getDifficulty().setPlayerPower(Integer.parseInt(playerPower.getText().toString()));
            config.getDifficulty().setMaxMonsterPower(Integer.parseInt(monsterPower.getText().toString()));
        }

        return v -> {
            Intent intent = new Intent();
            intent.putExtra(IntentKeys.CONFIG, config);
            setResult(RESULT_OK, intent);
            finish();
        };
    }
}
