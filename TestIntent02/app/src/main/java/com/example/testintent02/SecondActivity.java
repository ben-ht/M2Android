package com.example.testintent02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        TextView editText = findViewById(R.id.textView);
        editText.setText(intent.getStringExtra("Text"));

        Button upperCaseBtn = findViewById(R.id.upperCaseBtn);
        upperCaseBtn.setOnClickListener(UpperCaseButtonHandler());

        Button invertBtn = findViewById(R.id.invertBtn);
        invertBtn.setOnClickListener(InvertBtnHandler());
    }


    private View.OnClickListener UpperCaseButtonHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                TextView editText = findViewById(R.id.textView);
                result.putExtra("Text", editText.getText().toString().toUpperCase());
                setResult(RESULT_OK, result);
                finish();
            }
        };
    }

    private View.OnClickListener InvertBtnHandler(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                TextView editText = findViewById(R.id.textView);
                String reverseString = new StringBuilder(editText.getText().toString()).reverse().toString();
                result.putExtra("Text", reverseString);
                setResult(RESULT_OK, result);
                finish();
            }
        };
    }


}