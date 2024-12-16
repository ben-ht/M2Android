package com.example.tpimagehembert;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private ImageView image;

    private final ActivityResultLauncher<String> getImageUri = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    try {
                        callback(result);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
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

        image = findViewById(R.id.image);

        ImageButton uploadImageBtn = findViewById(R.id.uploadImage);
        uploadImageBtn.setOnClickListener(uploadImage());
    }

    private View.OnClickListener uploadImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getImageUri.launch("image/*");
            }
        };
    }

    private void chargerImage(Uri imageUri) throws FileNotFoundException {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inMutable = true;
        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri),
                null,
                option);
        image.setImageBitmap(bm);
    }

    public void callback(Uri imageUri) throws FileNotFoundException {
        TextView uriTextView = findViewById(R.id.uri);
        uriTextView.setText(imageUri.toString());

        chargerImage(imageUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (image.getDrawable() == null){
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.horizontal_mirror){
            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            image.setImageBitmap(horizontalMirror(bitmap));
            return true;
        } else if (id == R.id.vertical_mirror){
            Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            image.setImageBitmap(verticalMirror(bitmap));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap horizontalMirror(Bitmap original){
        int width = original.getWidth();
        int height = original.getHeight();

        Bitmap newImage = Bitmap.createBitmap(width,
                height,
                original.getConfig());

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                int pixelColor = original.getPixel(i, j);
                newImage.setPixel(width - i - 1, j, pixelColor);
            }
        }

        return newImage;
    }

    public Bitmap verticalMirror(Bitmap original){
        int width = original.getWidth();
        int height = original.getHeight();

        Bitmap newImage = Bitmap.createBitmap(width,
                height,
                original.getConfig());

        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                int pixelColor = original.getPixel(i, j);
                newImage.setPixel(i, height - j - 1, pixelColor);
            }
        }

        return newImage;
    }
}