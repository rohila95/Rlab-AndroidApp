package com.handson.odu.rlab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.model.Event;

import java.util.List;

/**
 * Created by rgudipati on 10/25/2016.
 */
public class EventAdapter extends BaseAdapter {
    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context=context;
        this.eventList=eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event=eventList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView=inflater.inflate(R.layout.events,null);
        TextView title= (TextView) listView.findViewById(R.id.eventTitle);
        title.setText(event.getTitle());
//        TextView desc= (TextView) listView.findViewById(R.id.eventDesc);
//        desc.setText(event.getEvent_message());
        ImageView img= (ImageView) listView.findViewById(R.id.imageIcon);
        img.setImageResource(R.drawable.alarm);
        img.setMaxHeight(64);
        img.setMaxWidth(64);
        TextView time= (TextView) listView.findViewById(R.id.eventTime);
        time.setText(event.getStart_time()+"--"+event.getEnd_time());
        return listView;
    }
}
