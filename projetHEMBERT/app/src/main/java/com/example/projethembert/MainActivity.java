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

public class MainActivity extends AppCompatActivity {
    private Player player;

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

        startGame();
        registerButtons();
    }


    private View.OnClickListener openFightActivity(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DungeonActivity.class);
                intent.putExtra(IntentKeys.PLAYER, player);
                startActivity(intent);
            }
        };
    }

    private void startGame(){
        player = new Player();
    }

    private void registerButtons(){
        int[] buttonsId = {R.id.dungeon_1, R.id.dungeon_2, R.id.dungeon_3,
        R.id.dungeon_4, R.id.dungeon_5, R.id.dungeon_6, R.id.dungeon_7,
        R.id.dungeon_8, R.id.dungeon_9, R.id.dungeon_10, R.id.dungeon_11,
        R.id.dungeon_12, R.id.dungeon_13, R.id.dungeon_14, R.id.dungeon_15,
        R.id.dungeon_16};

        for (int id : buttonsId){
            Button button = findViewById(id);
            button.setOnClickListener(openFightActivity());
        }
    }
}