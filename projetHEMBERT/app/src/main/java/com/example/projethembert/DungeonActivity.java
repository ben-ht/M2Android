package com.example.projethembert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DungeonActivity extends AppCompatActivity {
    private Opponent opponent;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dungeon);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        opponent = new Opponent();

        Intent intent = getIntent();
        player = (Player)intent.getExtras().get(IntentKeys.PLAYER);

        Button attackBtn = findViewById(R.id.attack);
        attackBtn.setOnClickListener(fight());

        Button fleeBtn = findViewById(R.id.flee);
        fleeBtn.setOnClickListener(flee());
    }

    private View.OnClickListener fight(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean playerWins = player.fight(opponent);
                if (playerWins){
                    opponent = null;
                }
                Intent intent = new Intent();
                intent.putExtra(IntentKeys.FIGHT_RESULT,
                        new FightResult(FightResult.FightResultEnum.WON,
                                "Vous avez remporté le combat."));
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }


    private View.OnClickListener flee(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.flee();
                setResult(RESULT_OK);
                finish();
            }
        };
    }

}