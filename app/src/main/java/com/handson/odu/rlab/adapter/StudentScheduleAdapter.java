package com.handson.odu.rlab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.model.StudentSchedule;

import java.util.List;

/**
 * Created by rgudipati on 3/14/2017.
 */

public class StudentScheduleAdapter extends BaseAdapter {
    private final Context context;
    private final List<StudentSchedule> studentScheduleList;
    StudentSchedule studentSchedule;
    String status;
    public StudentScheduleAdapter(Context context,List<StudentSchedule> studentScheduleList,String status)
    {
        this.context=context;
        this.studentScheduleList=studentScheduleList;
        this.status=status;
    }
    @Override
    public int getCount() {
        return studentScheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentScheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        studentSchedule=studentScheduleList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView=inflater.inflate(R.layout.student_scheduleloglist,null);
        TextView timeLog= (TextView) listView.findViewById(R.id.logTime);
        TextView desc= (TextView) listView.findViewById(R.id.logDesc);
        timeLog.setText(studentSchedule.getIn_time()+" : "+studentSchedule.getOut_time());
        timeLog.setTextColor(Color.BLACK);

        if(status.equals("schedule")) {
            desc.setText(studentSchedule.getDescription());
            desc.setTextColor(Color.BLACK);
        }
        return listView;
    }
}
