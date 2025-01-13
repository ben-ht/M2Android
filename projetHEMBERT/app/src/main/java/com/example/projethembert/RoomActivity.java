package com.example.projethembert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RoomActivity extends AppCompatActivity {
    private Opponent opponent;
    private Player player;
    private int room;

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

        Intent intent = getIntent();
        player = (Player)intent.getSerializableExtra(IntentKeys.PLAYER);
        opponent = (Opponent)intent.getSerializableExtra(IntentKeys.OPPONENT);
        room = intent.getIntExtra(IntentKeys.ROOM, -1);

        Button attackBtn = findViewById(R.id.attack);
        attackBtn.setOnClickListener(fight());

        Button fleeBtn = findViewById(R.id.flee);
        fleeBtn.setOnClickListener(fleeOnClick());

        getOnBackPressedDispatcher().addCallback(this, fleeOnBackButton());
    }

    private View.OnClickListener fight(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean playerWins = player.fight(opponent);

                Intent intent = new Intent();
                intent.putExtra(IntentKeys.PLAYER, player);
                if (playerWins){
                    intent.putExtra(IntentKeys.FIGHT_RESULT,
                            new FightResult(room, FightResult.FightResultEnum.WON));
                } else {
                    intent.putExtra(IntentKeys.FIGHT_RESULT,
                            new FightResult(room, FightResult.FightResultEnum.LOST));
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }


    private View.OnClickListener fleeOnClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flee();
            }
        };
    }

    private OnBackPressedCallback fleeOnBackButton(){
        return new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                flee();
            }
        };
    }

    private void flee(){
        player.flee();
        Intent intent = new Intent();
        intent.putExtra(IntentKeys.PLAYER, player);
        intent.putExtra(IntentKeys.FIGHT_RESULT,
                new FightResult(room, FightResult.FightResultEnum.FLED));
        setResult(RESULT_OK, intent);
        finish();
    }
}