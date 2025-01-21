package com.example.projethembert.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projethembert.R;
import com.example.projethembert.entities.enums.Difficulty;
import com.example.projethembert.utils.Config;
import com.example.projethembert.utils.IntentKeys;

import java.util.Objects;

/**
 * Page de configuration
 */
public class ConfigurationActivity extends AppCompatActivity {
    /// Configuration de la partie
    private Config config;

    private Config oldConfig;

    /// Bouton radio pour la configuration personnalisée
    private RadioButton customButton;

    /// Empêche customButton d'être sélectionné systématiquement
    private boolean isUserEditing = false;

    /// Input de la puissance max du monstre
    private EditText monsterPower;

    /// Input de la santé initiale du joueur
    private EditText playerHp;

    /// Input de la puissance initiale du joueur
    private EditText playerPower;

    /// Input du nom du joueur
    private EditText playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        config = intent.getParcelableExtra(IntentKeys.CONFIG);
        oldConfig = config;
        boolean isGameRunning = intent.getBooleanExtra(IntentKeys.IS_GAME_RUNNING, true);

        RadioGroup difficultyGroup = findViewById(R.id.difficultyGroup);
        customButton = findViewById(R.id.radio_custom);
        Button startGame = findViewById(R.id.startGame);
        Button backButton = findViewById(R.id.back);
        playerHp = findViewById(R.id.playerHealth);
        playerPower = findViewById(R.id.playerPower);
        monsterPower = findViewById(R.id.monsterPower);
        playerName = findViewById(R.id.playerName);
        playerName.setText(config.getPlayerName());

        startGame.setOnClickListener(v -> {
            if (isGameRunning && hasConfigChanged()){
                showRestartGameDialog();
            } else {
                startGame();
            }
        });

        backButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        difficultyGroup.setOnCheckedChangeListener((group, checkedId) ->
                onDifficultyChanged(checkedId));

        setDefaultValues(config.getDifficulty());
        initializeRadioButtons();

        addTextWatcher(playerHp);
        addTextWatcher(playerPower);
        addTextWatcher(monsterPower);
    }

    /// Sélectionne le bouton de configuration personnalisé si l'utilisateur écrit dans les champs
    private void addTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUserEditing) {
                    customButton.setChecked(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /// Sélectionne le radio correspondant à l'ouverture de la page
    private void initializeRadioButtons(){
        switch (config.getDifficulty()) {
            case EASY:
                ((RadioButton) findViewById(R.id.radio_easy)).setChecked(true);
                break;
            case MEDIUM:
                ((RadioButton) findViewById(R.id.radio_medium)).setChecked(true);
                break;
            case HARD:
                ((RadioButton) findViewById(R.id.radio_hard)).setChecked(true);
                break;
            default:
                customButton.setChecked(true);
        }
    }

    /// Remplit les valeurs par défaut au changement de difficulté
    private void onDifficultyChanged(int checkedId){
        if (!isUserEditing) {
            if (checkedId == R.id.radio_easy) setDefaultValues(Difficulty.EASY);
            else if (checkedId == R.id.radio_medium) setDefaultValues(Difficulty.MEDIUM);
            else if (checkedId == R.id.radio_hard) setDefaultValues(Difficulty.HARD);
            else setDefaultValues(Difficulty.CUSTOM);
        }
    }

    /// Remplit les valeurs par défaut d'une difficulté
    /// @param difficulty Niveau de difficulté choisi
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

    /**
     * Affiche une modale de redémarrage de partie. Le joueur peut annuler le changement de
     * configuration et continuer sa partie
     */
    private void showRestartGameDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.restart_game);
        dialogBuilder.setMessage(R.string.restart_game_warning);
        dialogBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            startGame();
        });
        dialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        dialogBuilder.create().show();
    }

    /**
     * Démarre une nouvelle partie avec la configuration sélectionnée
     */
    private void startGame() {
        if (config.getDifficulty() == Difficulty.CUSTOM) {
            config.getDifficulty().setPlayerHealth(Integer.parseInt(playerHp.getText().toString()));
            config.getDifficulty().setPlayerPower(Integer.parseInt(playerPower.getText().toString()));
            config.getDifficulty().setMaxMonsterPower(Integer.parseInt(monsterPower.getText().toString()));
        }

        config.setPlayerName(playerName.getText().toString());

        Intent intent = new Intent();
        intent.putExtra(IntentKeys.CONFIG, config);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean hasConfigChanged(){
        return !Objects.equals(config.getPlayerName(), oldConfig.getPlayerName())
                && config.getDifficulty().getName() == oldConfig.getDifficulty().getName()
                && config.getDifficulty().getPlayerHealth() == oldConfig.getDifficulty().getPlayerHealth()
                && config.getDifficulty().getPlayerPower() == oldConfig.getDifficulty().getPlayerPower()
                && config.getDifficulty().getMaxMonsterPower() == oldConfig.getDifficulty().getMaxMonsterPower();
    }
}
