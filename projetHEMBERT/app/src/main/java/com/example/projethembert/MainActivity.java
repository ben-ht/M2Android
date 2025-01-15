package com.example.projethembert;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projethembert.activities.RoomActivity;
import com.example.projethembert.entities.bonuses.BonusFactory;
import com.example.projethembert.entities.FightResult;
import com.example.projethembert.entities.Player;
import com.example.projethembert.entities.Room;
import com.example.projethembert.entities.enums.FightResultEnum;
import com.example.projethembert.utils.IntentKeys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Classe de la page d'accueil du jeu
 */
public class MainActivity extends AppCompatActivity {
    /// Nombre de salles
    private static final int NB_ROOMS = 16;

    /// Nombre de bonus
    private static final int NB_BONUSES = 2;

    ///RNG
    private static final Random random = new Random();

    /// Liste des salles
    private final ArrayList<Room> rooms = new ArrayList<>(NB_ROOMS);

    /// Message de résultat de combat
    private TextView fightResultLabel;

    /// Grille de ImageButton
    private GridLayout grid;
    /// Indicateur de points de vie
    ///
    private TextView health;

    /// Joueur
    private Player player;

    /// Indicateur de puissance
    private TextView power;

    /// Indicateur de salles non explorées
    private TextView unexploredRooms;

    /// Indicateur de niveau
    private TextView level;

    /// Bouton de passage à la manche suivante
    private ImageButton nextRound;

    /// Callback après le déroulement d'un combat
    private final ActivityResultLauncher<Intent> dungeonLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // TODO gestion erreur
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        player = intent.getParcelableExtra(IntentKeys.PLAYER);
                        FightResult fightResult = intent.getParcelableExtra(IntentKeys.FIGHT_RESULT);
                        handleFightResult(fightResult);
                    }
                }
            }
    );

    /**
     * Crée le menu d'options
     *
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Gère le clic sur les options du menu
     *
     * @param item The menu item that was selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            reset();
            return true;
        }
        if (item.getItemId() == R.id.quit) {
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialise l'activité
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
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

        player = new Player();

        fightResultLabel = findViewById(R.id.fight_result_content);
        unexploredRooms = findViewById(R.id.unexploredRooms);
        health = findViewById(R.id.health);
        power = findViewById(R.id.power);
        grid = findViewById(R.id.buttonsGrid);
        level = findViewById(R.id.level);
        nextRound = findViewById(R.id.nextRound);
        nextRound.setOnClickListener(v -> nextRound());

        init();
        createButtons();
        updatePlayerStats();
    }

    /**
     * Crée les boutons et les ajoute à la grille
     */
    private void createButtons() {
        for (int i = 0; i < NB_ROOMS; i++) {
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.drawable.door_monster);
            button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            button.setOnClickListener(openFightActivity(i + 1));

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
     * Désactive le clic sur les boutons des salles
     */
    private void disableButtonClick() {
        for (int i = 0; i < NB_ROOMS; i++) {
            grid.getChildAt(i).setEnabled(false);
        }
    }

    /**
     * Met à jour la page principale après le combat
     *
     * @param fightResult Résultat du combat
     */
    private void handleFightResult(FightResult fightResult) {
        GridLayout grid = findViewById(R.id.buttonsGrid);
        ImageButton clearedRoomBtn = (ImageButton) grid.getChildAt(fightResult.getRoomId() - 1);
        if (fightResult.getResult() == FightResultEnum.WON) {
            String unexploredRoomsCount = String.valueOf(Integer.parseInt(unexploredRooms.getText().toString()) - 1);
            unexploredRooms.setText(unexploredRoomsCount);
            rooms.set(fightResult.getRoomId() - 1, null);
            clearedRoomBtn.setImageResource(R.drawable.door_cross_mark);
        } else {
            clearedRoomBtn.setImageResource(R.drawable.door_dungeon);
        }

        updatePlayerStats();
        fightResultLabel.setText(fightResult.getResult().getMessage());
        tryEndGame();
    }

    /**
     * Initialise la page avec les données du joueur et les salles
     */
    private void init() {
        updatePlayerStats();
        BonusFactory.resetBonuses();
        player.setDefaultStats();
        HashSet<Integer> bonusesIndexes = new HashSet<>();
        for (int i = 0; i < NB_BONUSES; i++){
            int room = random.nextInt(NB_ROOMS);
            bonusesIndexes.add(room);
        }

        for (int i = 0; i < NB_ROOMS; i++) {
            rooms.add(i, new Room(i + 1, player.getLevel()));
            if (bonusesIndexes.contains(i)){
                rooms.get(i).setBonus(BonusFactory.createBonus());
            }
        }

        unexploredRooms.setText(String.valueOf(NB_ROOMS));
    }

    /**
     * Ouvre la page de combat
     *
     * @param index Indice de la salle (de 1 à NB_ROOM)
     */
    private View.OnClickListener openFightActivity(int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rooms.get(index - 1) == null) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.room_already_explored),
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                    intent.putExtra(IntentKeys.PLAYER, player);
                    intent.putExtra(IntentKeys.ROOM, rooms.get(index - 1));
                    dungeonLauncher.launch(intent);
                }
            }
        };
    }

    /**
     * Réinitialise le jeu à l'état de démarrage de l'application
     */
    private void reset() {
        player.setDefaultStats();
        init();

        fightResultLabel.setText(R.string.waiting_);

        for (int i = 0; i < NB_ROOMS; i++) {
            grid.getChildAt(i).setEnabled(true);
            ((ImageButton)grid.getChildAt(i)).setImageResource(R.drawable.door_monster);
        }
    }

    /**
     * Vérifie si la partie est terminée et affiche un message en conséquence
     */
    private void tryEndGame() {
        if (unexploredRooms.getText().equals("0")) {
            fightResultLabel.setText(R.string.victory);
            disableButtonClick();
            nextRound.setVisibility(View.VISIBLE);
        }

        if (player.getHealth() <= 0) {
            fightResultLabel.setText(R.string.defeat);
            disableButtonClick();
            player = new Player();
        }
    }

    private void updatePlayerStats(){
        health.setText(String.valueOf(player.getHealth()));
        power.setText(String.valueOf(player.getPower()));
        level.setText(getString(R.string.level, player.getLevel()));
    }

    private void nextRound(){
        nextRound.setVisibility(View.GONE);
        player.levelUp();
        reset();
    }
}