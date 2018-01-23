package com.xiey94.damp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FifthActivity extends AppCompatActivity {
    private View view1;
    private View view2;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        btn = findViewById(R.id.btn);
    }

    public void click(View view) {
        view1.layout(view1.getLeft(), view1.getTop(), view1.getRight(), view1.getBottom() - 100);

        btn.layout(btn.getLeft(), btn.getTop() - 100, btn.getRight(), btn.getBottom() - 100);
        view2.layout(view2.getLeft(), view2.getTop() - 100, view2.getRight(), view2.getBottom() - 100);

//        ViewGroup.LayoutParams params = view1.getLayoutParams();
//        params.height = view1.getBottom() - view1.getTop() - 100;
//        view1.setLayoutParams(params);

    }

}
