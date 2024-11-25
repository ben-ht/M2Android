package com.example.testintent02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){
                        Intent res = result.getData();
                        TextView text = findViewById(R.id.resultPreview);
                        String newText = null;
                        if (res != null) {
                            newText = res.getStringExtra("Text");
                        }
                        text.setText(newText);
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

        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(editBtnHandler());

        Button validateBtn = findViewById(R.id.validateBtn);
        validateBtn.setOnClickListener(validateBtnHandler());
    }

    private View.OnClickListener editBtnHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.editText);
                String textContent = text.getText().toString();

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("Text", textContent);
                getResult.launch(intent);
            }
        };
    }

    private View.OnClickListener validateBtnHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView result = findViewById(R.id.resultPreview);
                EditText text = findViewById(R.id.editText);
                text.setText(result.getText().toString());
            }
        };
    }
}