package com.taifua.hunnuphoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;


public class PhotoFragment extends Fragment
{
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;
    private String[] url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        mPhotoWall = view.findViewById(R.id.photo_wall);
        Images.init(getContext());
        if (!Images.imageThumbUrls.isEmpty())
        {
            url = new String[Images.imageThumbUrls.size()];
            adapter = new PhotoWallAdapter(getContext(), 0, Images.imageThumbUrls.toArray(url), mPhotoWall);
        }
        mPhotoWall.setAdapter(adapter);
        return view;
    }
}
