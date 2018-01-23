package com.xiey94.damp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.xiey94.damp.view.DampView;

public class FourthActivity extends AppCompatActivity {

    private DampView dampView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        dampView = findViewById(R.id.dampView);
        imageView = findViewById(R.id.imageView);
        dampView.setImageView(imageView);
    }
}
