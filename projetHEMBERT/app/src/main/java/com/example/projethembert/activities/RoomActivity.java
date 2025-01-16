package com.example.projethembert.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projethembert.entities.Room;
import com.example.projethembert.entities.enums.FightResultEnum;
import com.example.projethembert.utils.IntentKeys;
import com.example.projethembert.R;
import com.example.projethembert.entities.FightResult;
import com.example.projethembert.entities.Player;

/**
 * Page de combat
 */
public class RoomActivity extends AppCompatActivity {
    /// Joueur
    private Player player;

    /// Salle
    private Room room;

    /// Affichage de la santé du joueur
    private TextView health;

    /// Affichage de la puissance du joueur
    private TextView power;

    /// Affichage du bonus
    private ImageView bonusImage;

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
                boolean playerWins = player.fight(room.getMonster());

                Intent intent = new Intent();
                intent.putExtra(IntentKeys.PLAYER, player);
                if (playerWins){
                    intent.putExtra(IntentKeys.FIGHT_RESULT,
                            new FightResult(room.getId(), FightResultEnum.WON));
                } else {
                    intent.putExtra(IntentKeys.FIGHT_RESULT,
                            new FightResult(room.getId(), FightResultEnum.LOST));
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
                new FightResult(room.getId(), FightResultEnum.FLED));
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
        room = intent.getParcelableExtra(IntentKeys.ROOM);
        assert room != null;
        health = findViewById(R.id.health);
        power = findViewById(R.id.power);

        updatePlayerInfo();

        // TODO replace opponent
        TextView monsterPower = findViewById(R.id.opponent_power);
        monsterPower.setText(String.valueOf(room.getMonster().getPower()));

        ImageView monsterImage = findViewById(R.id.monsterImage);
        monsterImage.setImageResource(room.getMonster().getType().getAsset());

        TextView monsterType = findViewById(R.id.monsterType);
        monsterType.setText(room.getMonster().getType().getName());

        Button attackBtn = findViewById(R.id.attack);
        attackBtn.setOnClickListener(fight());

        Button fleeBtn = findViewById(R.id.flee);
        fleeBtn.setOnClickListener(fleeOnClick());

        getOnBackPressedDispatcher().addCallback(this, fleeOnBackButton());

        if (room.getBonus() != null){
            bonusImage = findViewById(R.id.bonusImage);
            bonusImage.setImageResource(room.getBonus().getImage());
            bonusImage.setVisibility(View.VISIBLE);
            bonusImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBonusDialog();
                }
            });
        }
    }

    private void showBonusDialog(){
         AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
         dialogBuilder.setTitle(room.getBonus().getName());
         dialogBuilder.setMessage(room.getBonus().getDescription());
         dialogBuilder.setPositiveButton(R.string.use, (dialog, which) -> {
            room.getBonus().use(player);
            bonusImage.setVisibility(View.GONE);
            updatePlayerInfo();

            dialog.dismiss();
         });
         dialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
         dialogBuilder.create().show();
    }

    private void updatePlayerInfo(){
        health.setText(String.valueOf(player.getHealth()));
        power.setText(String.valueOf(player.getPower()));
    }
}