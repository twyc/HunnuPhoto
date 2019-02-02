package com.taifua.hunnuphoto;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Images
{
    private static List<ImgFolderBean> imgFolderList;
    public static List<String> imageThumbUrls = new ArrayList<>();

    public static void init(Context context)
    {
        FileManager fileManager = FileManager.getInstance(context);
        imgFolderList = fileManager.getImageFolders();
        for (ImgFolderBean imgFolderBean : imgFolderList)
        {
            for (String s : fileManager.getImgListByDir(imgFolderBean.getDir()))
                imageThumbUrls.add(s);
        }
        Log.d("图片总共有多少: ", "" + imageThumbUrls.size());
    }
}
