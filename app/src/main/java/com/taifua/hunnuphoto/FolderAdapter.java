package com.taifua.hunnuphoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class FolderAdapter extends ArrayAdapter<ImgFolderBean> {

    private int resourceId;

    public FolderAdapter(Context context, int textViewResourceId,
                         List<ImgFolderBean> objects) {
        super(context, textViewResourceId, objects);
        Log.d("fuck", "适配器的构造函数没问题");
        Log.d("fuck", "resourceId = " + resourceId);
        resourceId = textViewResourceId;
    }
//这里还没有用viewholder进行优化
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageUtils imageUtils = new ImageUtils();
        ImgFolderBean imgFolder = getItem(position);
        View view;
        if ( convertView == null ){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else {
            view = convertView;
        }
        ImageView folderImage = (ImageView) view.findViewById(R.id.folder_image);
        TextView folderName = (TextView) view.findViewById(R.id.folder_name);
        TextView folderCnt = (TextView) view.findViewById(R.id.folder_imgCount);
        folderName.setText(imgFolder.getName());
        folderCnt.setText(""+imgFolder.getCount());

        Bitmap bitmap = imageUtils.getimage(imgFolder.getFistImgPath());
        folderImage.setImageBitmap(bitmap);
        return view;
    }
}
