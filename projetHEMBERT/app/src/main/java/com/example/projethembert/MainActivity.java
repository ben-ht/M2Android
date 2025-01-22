package com.example.projethembert;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projethembert.activities.ConfigurationActivity;
import com.example.projethembert.activities.LeaderboardActivity;
import com.example.projethembert.activities.RoomActivity;
import com.example.projethembert.entities.FightResult;
import com.example.projethembert.entities.LeaderboardEntry;
import com.example.projethembert.entities.Player;
import com.example.projethembert.entities.Room;
import com.example.projethembert.entities.bonuses.BonusFactory;
import com.example.projethembert.entities.enums.Difficulty;
import com.example.projethembert.entities.enums.FightResultEnum;
import com.example.projethembert.repository.Database;
import com.example.projethembert.utils.Config;
import com.example.projethembert.utils.IntentKeys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe de la page d'accueil du jeu
 */
public class MainActivity extends AppCompatActivity {
    /// Nombre de bonus
    private static final int NB_BONUSES = 2;
    /// Nombre de salles
    private static final int NB_ROOMS = 16;
    /// Nom des préférences
    private static final String PREFERENCES = "Preferences";
    /// Clé du nom d'utilisateur dans les préférences
    private static final String USERNAME_KEY = "username";
    /// RNG
    private static final Random random = new Random();
    /// Liste des salles
    private final ArrayList<Room> rooms = new ArrayList<>(NB_ROOMS);
    /// Permet d'éxécuter une tâche en asynchrone
    ExecutorService executor = Executors.newSingleThreadExecutor();
    /// Configuration de la partie
    private Config config;
    /// Message de résultat de combat
    private TextView fightResultContent;
    /// Grille de ImageButton
    private GridLayout grid;
    /// Indicateur de points de vie
    private TextView health;
    /// Indique si une partie est en cours
    private boolean isGameRunning;
    /// Indicateur de niveau
    private TextView level;
    /// Bouton de passage à la manche suivante
    private ImageButton nextRound; // TODO css
    /// Joueur
    private Player player;
    /// Indicateur de puissance
    private TextView power;
    /// Indicateur de salles non explorées
    private TextView remainingRooms;
    /// Callback après le déroulement d'un combat
    private final ActivityResultLauncher<Intent> dungeonLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        player = intent.getParcelableExtra(IntentKeys.PLAYER);
                        FightResult fightResult = intent.getParcelableExtra(IntentKeys.FIGHT_RESULT);
                        handleFightResult(fightResult);
                    } else {
                        Toast.makeText(MainActivity.this, "Une erreur s'est produite lors" +
                                        "du combat",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    /// Callback après l'ouverture de la page de configuration
    private final ActivityResultLauncher<Intent> configLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            config = intent.getParcelableExtra(IntentKeys.CONFIG);
                            TextView difficulty = findViewById(R.id.difficulty);
                            difficulty.setText(config.getDifficulty().getName());
                            reset(true);
                        }
                    }
                }
            }
    );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            openConfigActivity();
            return true;
        }
        if (item.getItemId() == R.id.leaderboard) {
            openLeaderboardActivity();
            return true;
        }
        if (item.getItemId() == R.id.reset) {
            reset(true);
            return true;
        }
        if (item.getItemId() == R.id.quit) {
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME_KEY, null);

        if (username == null) {
            showUsernameDialog(sharedPreferences);
        } else {
            initializeGame(sharedPreferences);
        }

        grid = findViewById(R.id.buttonsGrid);
        createButtons();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }

    /// Sélectionne un nombre (NB_BONUSES) d'Id de salles aléatoirements
    private HashSet<Integer> chooseRoomsToAddBonuses() {
        HashSet<Integer> indexes = new HashSet<>();
        for (int i = 0; i < NB_BONUSES; i++) {
            int room = random.nextInt(NB_ROOMS);
            indexes.add(room);
        }

        return indexes;
    }

    /// Supprime la salle lorsqu'elle est terminée
    private void clearRoom(int roomId) {
        rooms.set(roomId - 1, null);
    }

    /**
     * Crée les boutons et les ajoute à la grille
     */
    private void createButtons() {
        for (int i = 0; i < NB_ROOMS; i++) {
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.drawable.door_padlock);
            button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.secondary)));
            button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int finalI = i;
            button.setOnClickListener(v -> handleRoomBtnClick(finalI));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            params.columnSpec = GridLayout.spec(i % grid.getColumnCount(), 1f);
            params.rowSpec = GridLayout.spec(i / grid.getColumnCount(), 1f);
            params.setMargins(0, 16, 0, 16);
            params.setGravity(Gravity.CENTER);
            button.setLayoutParams(params);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(Color.BLACK);
            drawable.setCornerRadius(24f);
            drawable.setStroke(4, Color.BLACK);
            button.setBackground(drawable);

            grid.addView(button);
        }
    }

    /**
     * Met à jour la page principale après le combat
     *
     * @param fightResult Résultat du combat
     */
    private void handleFightResult(FightResult fightResult) {
        // Si l'on revient d'un combat alors la partie commence
        isGameRunning = true;

        updateRoomState(fightResult);

        updatePlayerStats();
        fightResultContent.setText(fightResult.getResult().getMessage());

        tryEndGame();
    }

    /// Met à jour l'UI en cas de défaite d'un combat
    private void handleLoss(ImageButton exploredRoomBtn) {
        exploredRoomBtn.setImageResource(R.drawable.door_dungeon);
    }

    /// Met à jour l'UI en cas de victoire
    private void handleWin(FightResult fightResult, ImageButton exploredRoomBtn) {
        updateRemainingRoomsCount();
        clearRoom(fightResult.getRoomId());
        exploredRoomBtn.setImageResource(R.drawable.door_cross);
    }

    /**
     * Initialise la page avec les données du joueur et les salles
     */
    private void initPlayerAndRooms() {
        player.setDefaultStats();
        updatePlayerStats();
        BonusFactory.resetBonuses();
        remainingRooms.setText(String.valueOf(NB_ROOMS));

        HashSet<Integer> bonusesIndexes = chooseRoomsToAddBonuses();

        for (int i = 0; i < NB_ROOMS; i++) {
            rooms.add(i, new Room(i + 1, player.getLevel(), config));
            if (bonusesIndexes.contains(i)) {
                rooms.get(i).setBonus(BonusFactory.createBonus());
            }
        }
    }

    /// Initialise le jeu à l'ouverture de l'application
    private void initializeGame(SharedPreferences sharedPreferences) {
        String username = sharedPreferences.getString(USERNAME_KEY, null);

        config = new Config(Difficulty.MEDIUM, username);
        player = new Player(config);

        fightResultContent = findViewById(R.id.fightResultContent);
        remainingRooms = findViewById(R.id.unexploredRooms);
        health = findViewById(R.id.health);
        power = findViewById(R.id.power);
        level = findViewById(R.id.level);
        nextRound = findViewById(R.id.nextRound);

        nextRound.setOnClickListener(v -> nextRound());

        initPlayerAndRooms();
    }

    /**
     * Gère le passage de la partie au niveau supérieur
     */
    private void nextRound() {
        nextRound.setVisibility(View.GONE);
        player.levelUp();
        reset(false);
    }

    /**
     * Ouvre la page de configuration
     */
    private void openConfigActivity() {
        Intent intent = new Intent(MainActivity.this, ConfigurationActivity.class);
        intent.putExtra(IntentKeys.IS_GAME_RUNNING, isGameRunning);
        intent.putExtra(IntentKeys.CONFIG, config);
        configLauncher.launch(intent);
    }

    /**
     * Ouvre la page de combat
     *
     * @param roomId Id de la salle (de 0 à NB_ROOM - 1)
     */
    private void openFightActivity(int roomId) {
        if (rooms.get(roomId) == null) {
            Toast.makeText(MainActivity.this,
                    getString(R.string.room_already_explored),
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            intent.putExtra(IntentKeys.PLAYER, player);
            intent.putExtra(IntentKeys.ROOM, rooms.get(roomId));
            dungeonLauncher.launch(intent);
        }
    }

    /**
     * Ouvre la page des scores
     */
    private void openLeaderboardActivity() {
        Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
        startActivity(intent);
    }

    /// Supprime le bonus d'une salle quand il a été utilisé
    private void removeBonusFromRoom(int roomId) {
        rooms.get(roomId - 1).setBonus(null);
    }

    /**
     * Réinitialise le jeu et l'interface
     *
     * @param newGame True si on recommence une partie, false si on recommence une manche
     */
    private void reset(boolean newGame) {
        resetGameState(newGame);
        initPlayerAndRooms();
        resetUIElements();
    }

    /**
     * Réinitialise l'état du jeu pour une nouvelle manche ou nouvelle partie
     *
     * @param newGame True si on recommence une partie, false si on recommence une manche
     */
    private void resetGameState(boolean newGame) {
        if (newGame) {
            isGameRunning = true;
            player = new Player(config);
        } else {
            player.setDefaultStats();
        }
    }

    /**
     * Réinitialise la grille de bouton et le résultat du combat sur l'UI
     */
    private void resetUIElements() {
        fightResultContent.setText(R.string.waiting_);

        makeRoomBtnClickable(true);

        for (int i = 0; i < NB_ROOMS; i++) {
            ((ImageButton) grid.getChildAt(i)).setImageResource(R.drawable.door_padlock);
        }
    }

    /**
     * Sauvegarde le score de la partie en bdd s'il fait partie des 10 meilleurs
     */
    private void saveScore() {
        LeaderboardEntry entry = new LeaderboardEntry(
                config.getPlayerName(),
                player.getLevel(),
                player.getPower(),
                new Date(),
                Difficulty.getInDatabaseName(
                        MainActivity.this,
                        getString(config.getDifficulty().getName()))
        );

        executor.execute(() -> {
            Database db = Database.getInstance(getApplicationContext());
            db.leaderboardRepository().insertIfTop10(entry);
        });
    }

    /// Affiche la modale pour indiquer le nom du joueur lors de la première ouverture
    /// de l'application
    private void showUsernameDialog(SharedPreferences preferences) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisis un nom d'utilisateur");
        EditText input = new EditText(this);
        input.setHint("Nom d'utilisateur");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String username = input.getText().toString().trim();
            if (!username.isBlank()) {
                preferences.edit().putString(USERNAME_KEY, username).apply();
                initializeGame(preferences);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!input.getText().toString().isBlank());

            }
        });
    }

    /**
     * Vérifie si la partie est terminée et affiche un message en conséquence
     */
    private void tryEndGame() {
        if (remainingRooms.getText().equals("0")) {
            fightResultContent.setText(R.string.victory);
            nextRound.setVisibility(View.VISIBLE);
            makeRoomBtnClickable(false);
        }

        if (player.getHealth() <= 0) {
            fightResultContent.setText(R.string.defeat);
            makeRoomBtnClickable(false);
            saveScore();
        }
    }

    /**
     * Active ou désactive le clic sur les boutons des salles
     * @param enable True s'il faut activer les boutons, false sinon
     */
    private void makeRoomBtnClickable(boolean enable){
        for (int i = 0; i < grid.getChildCount(); i++){
            grid.getChildAt(i).setEnabled(enable);
        }
    }

    /**
     * Met à jour les stats du joueur sur l'UI
     */
    private void updatePlayerStats() {
        health.setText(String.valueOf(player.getHealth()));
        power.setText(String.valueOf(player.getPower()));
        level.setText(getString(R.string.level_with_placeholder, player.getLevel()));
    }

    /// Met à jour le nombre de salles non explorées restantes
    private void updateRemainingRoomsCount() {
        String remainingRoomsCount = String.valueOf(Integer.parseInt(remainingRooms.getText().toString()) - 1);
        remainingRooms.setText(remainingRoomsCount);
    }

    /// Met à jour l'état de la salle après un combat
    private void updateRoomState(FightResult fightResult) {
        GridLayout grid = findViewById(R.id.buttonsGrid);
        ImageButton exploredRoomBtn = (ImageButton) grid.getChildAt(fightResult.getRoomId() - 1);

        if (fightResult.isBonusUsed()) {
            removeBonusFromRoom(fightResult.getRoomId());
        }

        if (fightResult.getResult() == FightResultEnum.WON) {
            handleWin(fightResult, exploredRoomBtn);
        } else {
            handleLoss(exploredRoomBtn);
        }
    }

    private void handleRoomBtnClick(int roomId){
        if (rooms.get(roomId) == null){
            Toast.makeText(MainActivity.this,
                    R.string.already_explored_msg,
                    Toast.LENGTH_SHORT).show();
        } else {
            openFightActivity(roomId);
        }
    }
}