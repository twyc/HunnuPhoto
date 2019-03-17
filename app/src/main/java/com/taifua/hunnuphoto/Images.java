package com.taifua.hunnuphoto;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Images
{
    private static List<ImgFolderBean> imgFolderList = new ArrayList<>();
    public static List<String> imageThumbUrls = new ArrayList<>();

    public static void init(Context context)
    {
        FileManager fileManager = FileManager.getInstance(context);
        imgFolderList = fileManager.getImageFolders();

    }

    public static void getImg(String dir, Context context){
        FileManager fileManager = FileManager.getInstance(context);
        imageThumbUrls.clear();
        for (String s : fileManager.getImgListByDir(dir)) {
            imageThumbUrls.add(s);
        }
    }
}
