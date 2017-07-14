package com.handson.odu.rlab.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.UI.StudentschedulelogActivity;
import com.handson.odu.rlab.model.TimeLog;

import java.util.List;

/**
 * Created by rgudipati on 3/1/2017.
 */

public class StudentTimeLogAdapter extends BaseAdapter {
    private final Context context;
    private final List<TimeLog> timeLogList;
    String userid;

    public StudentTimeLogAdapter(Context context,List<TimeLog> timeLogList,String userid)
    {
        this.context=context;
        this.timeLogList=timeLogList;
        this.userid=userid;
    }
    @Override
    public int getCount() {
        return timeLogList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeLogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       final TimeLog timeLog=timeLogList.get(position);
//        System.out.println(position+"---"+timeLog.getCurrent_date()+"--"+timeLog.getMinutes());
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView=inflater.inflate(R.layout.studentloglist,null);
        TextView day= (TextView) listView.findViewById(R.id.logDate);
        day.setText(timeLog.getCurrent_date());
        day.setTextColor(Color.BLACK);
        TextView logHours= (TextView) listView.findViewById(R.id.logDuration);
        logHours.setText(timeLog.getHours()+" hrs "+timeLog.getMinutes()+"min");
        logHours.setTextColor(Color.BLACK);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"On date "+ timeLog.getCurrent_date(),Toast.LENGTH_SHORT).show();
                Intent in=new Intent(context,StudentschedulelogActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("date",timeLog.getCurrent_date());
                in.putExtra("userid",userid);
                context.startActivity(in);
            }
        });
        return listView;
    }
}
