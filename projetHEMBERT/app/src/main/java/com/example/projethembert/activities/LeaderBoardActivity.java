package com.example.projethembert.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projethembert.R;
import com.example.projethembert.entities.LeaderboardEntry;
import com.example.projethembert.repository.Database;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LeaderBoardActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leader_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView lv = findViewById(R.id.leaderboardList);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<>()
        );
        lv.setAdapter(adapter);


        TabLayout tab = findViewById(R.id.difficultyTab);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String difficulty = tab.getText().toString();
                loadLeaderboard(difficulty);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadLeaderboard(String difficulty){
        AsyncTask.execute(() -> {
            Database db = Database.getInstance(getApplicationContext());
            List<LeaderboardEntry> entries = db.leaderboardRepository().getBestScores("Moyen");

            runOnUiThread(() -> {
                List<String> displayEntries = entriesToStringList(entries);
                adapter.clear();
                adapter.addAll(displayEntries);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private List<String> entriesToStringList(List<LeaderboardEntry> entries){
        List<String> strings = new ArrayList<>();
        strings.add(String.format(Locale.FRANCE,
                "%s %s %s %s %s",
                "Joueur", "Niveau", "Puissance", "Date"));
        for (LeaderboardEntry entry : entries){
            String str = String.format(Locale.FRANCE,
                    "%s %d %d %d %s",
                    entry.getPlayerName(),
                    entry.getLevelReached(),
                    entry.getPower(),
                    entry.getDate()
            );

            strings.add(str);
        }

        return strings;
    }
}