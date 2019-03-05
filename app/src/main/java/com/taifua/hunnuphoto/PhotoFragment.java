package com.taifua.hunnuphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class PhotoFragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        FileManager fileManager = FileManager.getInstance(getContext());
        final List<ImgFolderBean> list= fileManager.getImageFolders();
        FolderAdapter adapter = new FolderAdapter(getContext(), R.layout.folder_item, list);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ImgFolderBean imgFolderBean = list.get(position);
                Intent intent = new Intent(getContext(), ImgWallActivity.class);
                intent.putExtra("path", imgFolderBean.getDir());
                Toast.makeText(getContext(), imgFolderBean.getDir(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        return view;
    }
}
