package com.khoa.customview.blur_image;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.khoa.customview.R;

public class BlurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur_image);

        ImageView imgBlur = findViewById(R.id.img_blur);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        imgBlur.setBackground(new BitmapDrawable(getResources(), blurredBitmap));

    }
}
