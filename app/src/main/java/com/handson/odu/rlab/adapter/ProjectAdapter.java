package com.handson.odu.rlab.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.UI.HomeActivity;

import com.handson.odu.rlab.UI.ProjectupdateActivity;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.Project;
import com.handson.odu.rlab.model.ProjectMember;
import com.handson.odu.rlab.model.ProjectsList;
import com.handson.odu.rlab.model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgudipati on 10/24/2016.
 */
public class ProjectAdapter extends BaseAdapter{
    private Context context;
    private List<Project> projectList;
    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context=context;
        this.projectList=projectList;
    }

    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Project project=projectList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listView=inflater.inflate(R.layout.project_list,null);
        //TextView authorData= (TextView) listView.findViewById(R.id.authorData);
        //authorData.setText();
        TextView title= (TextView) listView.findViewById(R.id.projTitle);
        if(RLabApplication.getRole().equals("R.A"))
            title.setText(project.getProject_name());
        else  if(RLabApplication.getRole().equals("T.A"))
            title.setText(project.getProfessor_name());
        else if(RLabApplication.getRole().equals("Professor") && RLabApplication.Tag.equals("RA"))
            title.setText(project.getProject_name());
        else  if(RLabApplication.getRole().equals("Professor") && RLabApplication.Tag.equals("TA"))
            title.setText(project.getProfessor_name());
        TextView member= (TextView) listView.findViewById(R.id.projectMember);
        member.setText(TextUtils.join("\n",project.getStudents()));
        TextView update= (TextView) listView.findViewById(R.id.projUpdate);
        update.setSingleLine(false);
        update.setText(project.getMessage()+"\n\n-"+project.getUsername()+" at "+project.getTime_posted());
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"clickkkk11111",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(context,ProjectupdateActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(RLabApplication.getRole().equals("R.A"))
                        in.putExtra("title",project.getProject_name());
                else  if(RLabApplication.getRole().equals("T.A")) {
                    in.putExtra("title", project.getProject_name());
//                    System.out.println("proj name---"+project.getProfessor_name());
                }
                else if(RLabApplication.getRole().equals("Professor") && RLabApplication.Tag.equals("RA"))
                    in.putExtra("title",project.getProject_name());
                else  if(RLabApplication.getRole().equals("Professor") && RLabApplication.Tag.equals("TA"))
                    in.putExtra("title",project.getProject_name());
                //in.putExtra("id",project.getProjectId);
                in.putExtra("prevUpdate",project.getMessage());
                in.putExtra("members",project.getStudents());
                in.putExtra("user",project.getUsername());
                in.putExtra("time",project.getTime_posted());
//                EditText newupdate= (EditText) listView.findViewById(R.id.newUpdate);
//                newupdate.setVisibility(View.VISIBLE);
//                Button submit= (Button) listView.findViewById(R.id.update);
//                submit.setVisibility(View.VISIBLE);
                context.startActivity(in);
            }
        });
//        EditText newupdate= (EditText) listView.findViewById(R.id.newUpdate);
//        newupdate.setVisibility(View.GONE);
//        Button submit= (Button) listView.findViewById(R.id.update);
//        submit.setVisibility(View.GONE);
        return listView;
    }
}
