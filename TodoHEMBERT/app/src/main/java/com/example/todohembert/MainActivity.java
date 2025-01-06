package com.example.todohembert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    // Clé de l'intention de la tâche
    public static final String KEY_TASK = "task";

    // Délimiteur du fichier de sauvegarde
    private static final String COMMA_DELIMITER = ",";

    // Elements de la liste
    private final ArrayList<String> items = new ArrayList<>();

    //Element UI
    private ArrayAdapter<String> adapter;
    private ListView lv;

    // Callback de l'activité d'ajout de tâche
    private final ActivityResultLauncher<Intent> addTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == MainActivity.RESULT_OK){
                        Intent taskIntent = result.getData();
                        assert taskIntent != null;
                        String taskLabel = taskIntent.getStringExtra(KEY_TASK);
                        items.add(taskLabel);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
    );

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

        loadItems();
        initUI();
    }

    /**
     * Initalise les éléments d'interface (adapter, eventListeners...)
     */
    private void initUI(){
        FloatingActionButton addItemButton = findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(launchAddTaskActivity());

        lv = findViewById(R.id.itemList);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setItemsCanFocus(false);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_checked,
                items);
        lv.setAdapter(adapter);
    }

    /**
     * Crée le menu d'options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Gère les clics sur le menu d'options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteItemsButton) {
            deleteItems();
            return true;
        }
        if (id == R.id.saveItemsButton){
            saveItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Démarre l'activité d'ajout de tâche
     */
    private View.OnClickListener launchAddTaskActivity(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                addTaskLauncher.launch(intent);
            }
        };
    }

    /**
     * Supprime les élements sélectionnés de la liste
     */
    private void deleteItems(){
        SparseBooleanArray toDelete = lv.getCheckedItemPositions();
        if (toDelete == null || toDelete.size() == 0) {
            return;
        }

        for (int i = lv.getCount(); i >=0; i--){
            if (toDelete.get(i)){
                items.remove(i);
            }
        }

        adapter.notifyDataSetChanged();
        lv.clearChoices();
    }

    /**
     * Charge la liste avec le contenu d'un fichier csv
     */
    private void loadItems() {
        try {
            InputStream stream = openFileInput("tasks.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line = reader.readLine();
            String[] items = line.split(COMMA_DELIMITER);
            this.items.addAll(Arrays.asList(items));
        } catch (Exception ex){
            Toast.makeText(MainActivity.this,
                    "Failed to load tasks",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sauvegarde la liste dans un fichier csv
     */
    private void saveItems() {
        try {
            OutputStream stream = openFileOutput("tasks.csv", Context.MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
            for (int i = 0; i < items.size(); i++) {
                if (i == items.size() - 1) {
                    writer.write(items.get(i));
                } else {
                    writer.write(items.get(i) + ",");
                }
            }
            writer.close();
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this,
                    ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}