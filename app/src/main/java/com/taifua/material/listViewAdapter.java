package com.taifua.material;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class listViewAdapter extends BaseAdapter
{
    private Context context;
    private List<String> list;
    private LayoutInflater layoutInflater;

    public listViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return list.size();
    }
    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view;
        view = layoutInflater.inflate(R.layout.listview_item, null);
        return view;
    }
}
