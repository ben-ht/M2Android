package com.example.projethembert.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.projethembert.utils.LeaderboardAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Page de scores
 */
public class LeaderboardActivity extends AppCompatActivity {

    private LeaderboardAdapter adapter;

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
        adapter = new LeaderboardAdapter(this);
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.leaderboard_header, lv, false);
        lv.addHeaderView(header);
        lv.setAdapter(adapter);


        TabLayout tab = findViewById(R.id.difficultyTab);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO switch case
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

    /**
     * Remplit le tableau avec les données de la bdd
     * @param difficulty Difficulté sélectionnée sur l'onglet
     */
    private void loadLeaderboard(String difficulty){
        AsyncTask.execute(() -> {
            Database db = Database.getInstance(getApplicationContext());
            List<LeaderboardEntry> entries = db.leaderboardRepository().getBestScores(difficulty);

            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(entries);
                adapter.notifyDataSetChanged();
            });
        });
    }
}