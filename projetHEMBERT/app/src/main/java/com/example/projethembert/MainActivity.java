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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
                        player = intent.getParcelableExtra(IntentKeys.PLAYER);
                        FightResult fightResult = intent.getParcelableExtra(IntentKeys.FIGHT_RESULT);
                        handleFightResult(fightResult);
                    }
                }
            }
    );

    private void handleFightResult(FightResult fightResult) {
        GridLayout grid = findViewById(R.id.buttonsGrid);
        ImageButton clearedRoomBtn = (ImageButton) grid.getChildAt(fightResult.room - 1);
        if (fightResult.result == FightResult.FightResultEnum.WON) {
            String unexploredRoomsCount = String.valueOf(Integer.parseInt(unexploredRooms.getText().toString()) - 1);
            unexploredRooms.setText(unexploredRoomsCount);
            opponents.set(fightResult.room - 1, null);
            clearedRoomBtn.setImageResource(R.drawable.cross_mark);
            power.setText(String.valueOf(player.getPower()));
        } else {
            clearedRoomBtn.setImageResource(R.drawable.dungeon_gate);
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
            opponents.add(i, new Opponent());
        }

        unexploredRooms.setText(String.valueOf(NB_ROOMS));
        health.setText(String.valueOf(player.getHealth()));
        power.setText(String.valueOf(player.getPower()));
    }

    private void createButtons(){
        for (int i = 0; i < NB_ROOMS; i++){
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.drawable.diablo_skull);
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

    private void disableButtonClick(){
        for (int i = 0; i < NB_ROOMS; i++){
            grid.getChildAt(i).setEnabled(false);
        }
    }

    private void tryEndGame(){
        if (unexploredRooms.getText() == "0"){
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
            ((ImageButton)grid.getChildAt(i)).setImageResource(R.drawable.diablo_skull);
        }
    }
}