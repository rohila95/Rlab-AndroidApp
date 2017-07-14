package com.handson.odu.rlab.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.ProjStatusAdapter;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.ProjUpdateList;
import com.handson.odu.rlab.model.SimpleStudent;
import com.handson.odu.rlab.model.User;
import com.handson.odu.rlab.model.UserList;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class ProjectupdateActivity extends AppCompatActivity {

    ProjUpdateList projUpdateList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectupdate);


        TextView title= (TextView) findViewById(R.id.title);
//        TextView prevUpdate= (TextView) findViewById(R.id.prevUpdate);
//        TextView author= (TextView) findViewById(R.id.author);


        title.setText(getIntent().getStringExtra("title"));
//        prevUpdate.setText(getIntent().getStringExtra("prevUpdate"));
//        author.setText("-"+getIntent().getStringExtra("user")+" at "+getIntent().getStringExtra("time"));

        new RequestAllUpdates().execute(getIntent().getStringExtra("title"));
        ImageView go= (ImageView) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText update= (EditText) findViewById(R.id.update);
                System.out.println("update is "+update.getText());
                new SendUpdate().execute(getIntent().getStringExtra("title"),update.getText().toString(), RLabApplication.getId());
            }
        });


    }
    public class RequestAllUpdates extends AsyncTask<String,Integer,String>
    {
        //public ProgressDialog dialog=new ProgressDialog(getParent().getApplicationContext());
        @Override
        protected void onPreExecute() {
//            dialog.setMessage("Loading students...");
//            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("projname",params[0]);

            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetUpdateLog.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from project service is "+result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    projUpdateList = mapper.readValue(result, ProjUpdateList.class);
                    ListView listView = (ListView) findViewById(R.id.prevUpdates);
                    ProjStatusAdapter projStatusAdapter = new ProjStatusAdapter(getApplicationContext(), projUpdateList.getProj_updates());
                    listView.setAdapter(projStatusAdapter);
                    listView.setDivider(null);
                    listView.setDividerHeight(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error loading updates.",Toast.LENGTH_SHORT).show();
            }
          //  dialog.cancel();
        }
    }
    public class SendUpdate extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(ProjectupdateActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("updating...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("projname",params[0]);
            map.put("update",params[1]);
            map.put("userid",params[2]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/newUpdate.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from project update service is "+result);
            if(!result.contains("Exception"))
            {
                Toast.makeText(getApplicationContext(),"Update for project added successfully.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error in adding update.",Toast.LENGTH_SHORT).show();
            }
            Intent intent = getIntent();
            finish();
            startActivity(intent);

              dialog.cancel();
        }
    }
}
