package com.example.projethembert.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projethembert.entities.enums.FightResultEnum;
import com.example.projethembert.utils.IntentKeys;
import com.example.projethembert.R;
import com.example.projethembert.entities.FightResult;
import com.example.projethembert.entities.Monster;
import com.example.projethembert.entities.Player;

/**
 * Page de combat
 */
public class RoomActivity extends AppCompatActivity {
    /// Monstre
    private Monster monster;

    /// Joueur
    private Player player;

    /// Index de la salle
    private int room;

    /**
     * Initialisation de l'activité
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();
    }

    /**
     * Effectue le combat et retourne le résultat à l'activité principale
     * @return Résultat du combat
     */
    private View.OnClickListener fight(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean playerWins = player.fight(monster);

                Intent intent = new Intent();
                intent.putExtra(IntentKeys.PLAYER, player);
                if (playerWins){
                    intent.putExtra(IntentKeys.FIGHT_RESULT,
                            new FightResult(room, FightResultEnum.WON));
                } else {
                    intent.putExtra(IntentKeys.FIGHT_RESULT,
                            new FightResult(room, FightResultEnum.LOST));
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }

    /**
     * Retourne la fuite à l'activité principale
     */
    private void flee(){
        player.flee();
        Intent intent = new Intent();
        intent.putExtra(IntentKeys.PLAYER, player);
        intent.putExtra(IntentKeys.FIGHT_RESULT,
                new FightResult(room, FightResultEnum.FLED));
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Effectue une fuite à l'appui sur le bouton retour
     */
    private OnBackPressedCallback fleeOnBackButton(){
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                flee();
            }
        };
    }

    /**
     * Effectue une fuite au clic sur le bouton Fuir
     */
    private View.OnClickListener fleeOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flee();
            }
        };
    }

    /**
     * Initialise la page (textviews, events...)
     */
    private void initUI(){
        Intent intent = getIntent();
        player = intent.getParcelableExtra(IntentKeys.PLAYER);
        monster = intent.getParcelableExtra(IntentKeys.OPPONENT);
        room = intent.getIntExtra(IntentKeys.ROOM, -1);

        TextView health = findViewById(R.id.health);
        health.setText(String.valueOf(player.getHealth()));

        TextView power = findViewById(R.id.power);
        power.setText(String.valueOf(player.getPower()));

        TextView opponentPower = findViewById(R.id.opponent_power);
        opponentPower.setText(String.valueOf(monster.getPower()));

        ImageView monsterImage = findViewById(R.id.monsterImage);
        monsterImage.setImageResource(monster.getType().getAsset());

        TextView monsterType = findViewById(R.id.monsterType);
        monsterType.setText(monster.getType().getName());

        Button attackBtn = findViewById(R.id.attack);
        attackBtn.setOnClickListener(fight());

        Button fleeBtn = findViewById(R.id.flee);
        fleeBtn.setOnClickListener(fleeOnClick());

        getOnBackPressedDispatcher().addCallback(this, fleeOnBackButton());
    }
}