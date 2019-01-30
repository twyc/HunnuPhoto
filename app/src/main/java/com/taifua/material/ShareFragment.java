package com.taifua.material;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ShareFragment extends Fragment {
    private SearchView searchView;
    private ListView listView;
    private GridView mPhotoWall;
    private List<String> list;
    private List<String> findList;
    private List<String> nameList;
    private PhotoWallAdapter padapter;
    private listViewAdapter adapter;
    private listViewAdapter findAdapter;
    private Bitmap bitmap;
    private MatchString matchString = new MatchString();
    private String []url;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 123:
//                    adapter = new listViewAdapter(getContext(), list);
//                    listView.setAdapter(adapter);
                    for(int i = 0; i < list.size(); i++) {
                        String information = list.get(i);
//                        nameList.add(information);
                    }
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        super.onCreate(savedInstanceState);
        mPhotoWall = (GridView) view.findViewById(R.id.photo4wall);
        searchView = (SearchView) view.findViewById(R.id.searchEdit);
        listView = (ListView) view.findViewById(R.id.listView);
        findList = new ArrayList<String>();

        /**
         * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
         * true)来添加一个提交按钮（"submit" button)
         * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
         */
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query) {

                if(TextUtils.isEmpty(query)) {
                    Toast.makeText(getContext(), "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    listView.setAdapter(adapter);
                }
                else {
                    findList.clear();
                    findList = matchString.bf(query);
                    if(findList.size() == 0) {
                        Toast.makeText(getContext(), "没有这个文件夹或者图片", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "查找成功", Toast.LENGTH_SHORT).show();
                        url = new String[findList.size()];
                        padapter = new PhotoWallAdapter(getContext(), 0, findList.toArray(url), mPhotoWall);
                        mPhotoWall.setAdapter(padapter);
//                        findAdapter = new listViewAdapter(getContext(), findList);
//                        listView.setAdapter(findAdapter);
                    }
                }
                return true;
            }

            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)) {
                    listView.setAdapter(adapter);
                }
                else {
                    findList.clear();
                    for(int i = 0; i < list.size(); i++) {
                        String information = list.get(i);
                        if(information.contains(newText)) {
                            findList.add(information);
                        }
                    }
                    findAdapter = new listViewAdapter(getContext(), findList);
                    findAdapter.notifyDataSetChanged();
                    listView.setAdapter(findAdapter);
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            public void run() {
                list = Images.imageThumbUrls;
                Message message = new Message();
                message.what = 123;
                message.obj = list;
                handler.sendMessage(message);
            }
        }).start();
        return view;
    }
}
