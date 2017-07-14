package com.handson.odu.rlab.UI;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.StudentTimeLogAdapter;
import com.handson.odu.rlab.model.TimeLogList;
import com.handson.odu.rlab.utility.RequestService;

import java.util.HashMap;

public class StudentlogActivity extends AppCompatActivity {
String selectedUserid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlog);
        selectedUserid=getIntent().getStringExtra("id");
        new RequestStudentLogDetails().execute(getIntent().getStringExtra("id"));

    }
    public class RequestStudentLogDetails extends AsyncTask<String,Integer,String>
    {
       // public ProgressDialog dialog=new ProgressDialog(getApplicationContext());
        @Override
        protected void onPreExecute() {
//            dialog.setMessage("Loading logs...");
//            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid",params[0]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetAvailabilityLog.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Student time log service is "+result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();

                try {
                    TimeLogList timeLogList = mapper.readValue(result, TimeLogList.class);
                    ListView listView = (ListView) findViewById(R.id.studentLog);
                    StudentTimeLogAdapter studentTimeLogAdapter = new StudentTimeLogAdapter(getApplicationContext(), timeLogList.getTotal_time(), selectedUserid);
                    listView.setAdapter(studentTimeLogAdapter);
                    listView.setDividerHeight(0);

//implement onclick..........

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error retrieving time log.",Toast.LENGTH_SHORT).show();
            }
//            dialog.cancel();
        }
    }
}
