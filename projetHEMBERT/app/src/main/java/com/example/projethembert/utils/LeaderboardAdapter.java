package com.example.projethembert.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projethembert.R;
import com.example.projethembert.entities.LeaderboardEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter pour afficher le tableau des scores
 */
public class LeaderboardAdapter extends ArrayAdapter<LeaderboardEntry> {
    private final LayoutInflater inflater;

    public LeaderboardAdapter(Context context) {
        super(context, 0, new ArrayList<>());
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.leaderboard_row, parent, false);
        }

        LeaderboardEntry entry = getItem(position);

        TextView playerName = convertView.findViewById(R.id.playerName);
        TextView levelReached = convertView.findViewById(R.id.levelReached);
        TextView power = convertView.findViewById(R.id.power);
        TextView date = convertView.findViewById(R.id.date);

        if (entry != null) {
            playerName.setText(entry.getPlayerName());
            levelReached.setText(String.valueOf(entry.getLevelReached()));
            power.setText(String.valueOf(entry.getPower()));
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            date.setText(df.format(entry.getDate()));
        }

        return convertView;
    }
}
