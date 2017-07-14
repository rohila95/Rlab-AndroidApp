package com.handson.odu.rlab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handson.odu.rlab.R;

import com.handson.odu.rlab.model.ProjectMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgudipati on 10/24/2016.
 */
public class ProjectmembersAdapter extends BaseAdapter{
    Context context;
List<ProjectMember> members=new ArrayList<ProjectMember>();

    public ProjectmembersAdapter(Context context, List<ProjectMember> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProjectMember projectMember=members.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView=inflater.inflate(R.layout.members,null);
        TextView name= (TextView) listView.findViewById(R.id.memberName);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        name.setText(projectMember.getName());
        return listView;
    }
}
