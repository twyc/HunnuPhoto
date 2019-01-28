package com.taifua.material;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;


public class PhotoFragment extends Fragment {
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;
    private String []url;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        mPhotoWall = (GridView) view.findViewById(R.id.photo_wall);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(),"已经授权",Toast.LENGTH_LONG).show();
        } else {//请求权限方法
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        Images.init(getContext());
        if(!Images.imageThumbUrls.isEmpty()) {
            url = new String[Images.imageThumbUrls.size()];
            adapter = new PhotoWallAdapter(getContext(), 0, Images.imageThumbUrls.toArray(url), mPhotoWall);
        }
        mPhotoWall.setAdapter(adapter);
        return view;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
        }
    }
}
