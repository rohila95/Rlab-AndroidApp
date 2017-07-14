package com.handson.odu.rlab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.model.ProjUpdateList;
import com.handson.odu.rlab.model.ProjUpdates;

import java.util.List;

/**
 * Created by rgudipati on 2/10/2017.
 */

public class ProjStatusAdapter extends BaseAdapter {
    private Context context;
    private List<ProjUpdates> projUpdateList;
    public ProjStatusAdapter(Context context,List<ProjUpdates> projUpdateList)
    {
        this.context=context;
        this.projUpdateList=projUpdateList;
    }

    @Override
    public int getCount() {
        return projUpdateList.size();
    }

    @Override
    public Object getItem(int position) {
        return projUpdateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProjUpdates update = projUpdateList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView=inflater.inflate(R.layout.proj_updates,null);
        TextView prevUpdate= (TextView) listView.findViewById(R.id.prevUpdate);
        prevUpdate.setText(update.getMessage());
        prevUpdate.setTextColor(Color.BLACK);
        TextView author= (TextView) listView.findViewById(R.id.author);
        author.setText(update.getUsername()+" at "+update.getTime_posted());
        author.setTextColor(Color.BLACK);
        return listView;
    }
}
