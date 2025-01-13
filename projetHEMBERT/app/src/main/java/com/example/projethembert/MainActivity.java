package com.example.projethembert;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
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

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int NB_ROOMS = 16;
    private Player player;
    private final ArrayList<Opponent> opponents = new ArrayList<>(NB_ROOMS);
    private TextView unexploredRooms;
    private TextView health;
    private TextView power;
    private TextView fightResultLabel;
    private GridLayout grid;


    private final ActivityResultLauncher<Intent> dungeonLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent intent = result.getData();
                        player = (Player)intent.getSerializableExtra(IntentKeys.PLAYER);
                        FightResult fightResult = (FightResult) intent.getSerializableExtra(IntentKeys.FIGHT_RESULT);
                        handleFightResult(fightResult);
                    }
                }
            }
    );

    private void handleFightResult(FightResult fightResult) {
        GridLayout grid = findViewById(R.id.buttonsGrid);
        if (fightResult.result == FightResult.FightResultEnum.WON) {
            String unexploredRoomsCount = String.valueOf(Integer.parseInt(unexploredRooms.getText().toString()) - 1);
            unexploredRooms.setText(unexploredRoomsCount);
            opponents.set(fightResult.room - 1, null);
            Button clearedRoomBtn = (Button) grid.getChildAt(fightResult.room - 1);
            clearedRoomBtn.setText("X");
            power.setText(String.valueOf(player.getPower()));
        } else {
            health.setText(String.valueOf(player.getHealth()));
        }

        fightResultLabel.setText(fightResult.message);
        tryEndGame();
    }

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

        fightResultLabel = findViewById(R.id.fight_result_content);
        unexploredRooms = findViewById(R.id.unexploredRooms);
        health = findViewById(R.id.health);
        power = findViewById(R.id.power);
        grid = findViewById(R.id.buttonsGrid);

        init();
        createButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            reset();
            return true;
        }
        if (item.getItemId() == R.id.quit){
            finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener openFightActivity(int index){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opponents.get(index - 1) == null){
                    Toast.makeText(MainActivity.this,
                            "Cette salle à déja été explorée",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, RoomActivity.class);
                    intent.putExtra(IntentKeys.ROOM, index);
                    intent.putExtra(IntentKeys.PLAYER, player);
                    intent.putExtra(IntentKeys.OPPONENT, opponents.get(index - 1));
                    dungeonLauncher.launch(intent);
                }
            }
        };
    }

    private void init(){
        player = new Player();
        for (int i = 0; i < NB_ROOMS; i++){
            opponents.add(new Opponent());
        }

        unexploredRooms.setText(String.valueOf(NB_ROOMS));
        health.setText(String.valueOf(player.getHealth()));
        power.setText(String.valueOf(player.getPower()));
    }

    private void createButtons(){
        for (int i = 0; i < NB_ROOMS; i++){
            Button button = new Button(this);
            button.setText(String.valueOf(i + 1));
            button.setOnClickListener(openFightActivity(i + 1));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
            params.setMargins(16, 16, 16, 16);
            button.setLayoutParams(params);

            grid.addView(button);
        }
    }

    private void disableButtonClick(){
        for (int i = 0; i < NB_ROOMS; i++){
            grid.getChildAt(i).setEnabled(false);
        }
    }

    private void tryEndGame(){
        if (opponents.stream().allMatch(Objects::isNull)){
            fightResultLabel.setText("Victoire");
            disableButtonClick();
        }

        if (player.getHealth() <= 0){
            fightResultLabel.setText("Défaite");
            disableButtonClick();
        }
    }

    private void reset(){
        init();

        fightResultLabel.setText("En attente...");

        for (int i = 0; i < NB_ROOMS; i++){
            grid.getChildAt(i).setEnabled(true);
            ((Button)grid.getChildAt(i)).setText(String.valueOf(i + 1));
        }
    }
}