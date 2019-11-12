package com.shayannasir.projectm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainGame extends AppCompatActivity {

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGame.this, startGame.class);
                startActivity(intent);
                finish();
            }
        });

    }

//    public void startGame (View view)
//    {
//        Intent intent = new Intent(this, startGame.class);
//        startActivity(intent);
//        finish();
//
//    }
}
