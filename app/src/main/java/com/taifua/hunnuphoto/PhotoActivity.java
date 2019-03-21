package com.taifua.hunnuphoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        ImageView photo = (ImageView) findViewById(R.id.bigPhoto);
        ImageUtils imageUtils = new ImageUtils();
        photo.setImageBitmap(imageUtils.getImage(url));
    }
}
