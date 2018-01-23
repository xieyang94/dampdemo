package com.xiey94.damp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xiey94.damp.view.MyScrollView;

public class SecondActivity extends AppCompatActivity {

    private MyScrollView myScrollView;
    private ImageView imageView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        myScrollView = findViewById(R.id.myScrollView);
        imageView = findViewById(R.id.image);
        myScrollView.setImageView(imageView);
    }
}
