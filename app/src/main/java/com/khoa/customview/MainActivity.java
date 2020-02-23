package com.khoa.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.khoa.customview.blur_image.BlurActivity;
import com.khoa.customview.selectable_indicator.SelectableIndicatorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void openBlurImageActivity(View view){
        Intent intent = new Intent(MainActivity.this, BlurActivity.class);
        startActivity(intent);
    }

    public void openSelectableIndicatorActivity(View view){
        Intent intent = new Intent(MainActivity.this, SelectableIndicatorActivity.class);
        startActivity(intent);
    }
}
