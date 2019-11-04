package com.shayannasir.projectm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageViewerActivity extends AppCompatActivity {

    private ImageView imageView;

    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imageView = (ImageView)findViewById(R.id.image_viewer);

        imageURL = getIntent().getStringExtra("url");

        Picasso.get().load(imageURL).into(imageView);
    }
}
