package com.taifua.hunnuphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

public class ImgWallActivity extends AppCompatActivity {
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;
    private String[] url;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("fuck", "进来了");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photowall);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Images.getImg(path, this);
        mPhotoWall = (GridView)findViewById(R.id.photo_wall);
        if (!Images.imageThumbUrls.isEmpty())
        {
            url = new String[Images.imageThumbUrls.size()];
            adapter = new PhotoWallAdapter(this, 0, Images.imageThumbUrls.toArray(url), mPhotoWall);
        }
        mPhotoWall.setAdapter(adapter);
    }
}
