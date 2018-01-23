package com.xiey94.damp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xiey94.damp.view.PersonalScrollView;

public class ThirdActivity extends AppCompatActivity {

    private PersonalScrollView personalScrollView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        personalScrollView = findViewById(R.id.personalScrollView);
        imageView = findViewById(R.id.image);
        personalScrollView.setImageView(imageView);
    }
}
