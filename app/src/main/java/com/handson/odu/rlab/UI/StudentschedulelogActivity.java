package com.handson.odu.rlab.UI;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.StudentScheduleAdapter;
import com.handson.odu.rlab.model.StudentScheduleList;
import com.handson.odu.rlab.utility.RequestService;

import java.util.HashMap;

public class StudentschedulelogActivity extends AppCompatActivity {
    ListView stuSchedule,stuLog;
    String date,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentschedulelog);
        date=getIntent().getStringExtra("date");
        userid=getIntent().getStringExtra("userid");
        stuSchedule= (ListView) findViewById(R.id.stuSchedule);
        stuLog= (ListView) findViewById(R.id.stuLog);
        new RequestStudentScheduleLogDetails().execute(date,userid);
    }

    public class RequestStudentScheduleLogDetails extends AsyncTask<String,Integer,String>
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
            map.put("date",params[0]);
            map.put("userid",params[1]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetStudentSchedule.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Student time log service is "+result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();

                try {
                    StudentScheduleList timeLogList = mapper.readValue(result, StudentScheduleList.class);
                    if (timeLogList.getSchedule_data() != null) {
                        StudentScheduleAdapter studentScheduleAdapter = new StudentScheduleAdapter(getApplicationContext(), timeLogList.getSchedule_data(), "schedule");
                        stuSchedule.setAdapter(studentScheduleAdapter);
                    }
                    if (timeLogList.getAvailability_data() != null) {
                        StudentScheduleAdapter studentScheduleAdapter1 = new StudentScheduleAdapter(getApplicationContext(), timeLogList.getAvailability_data(), "log");
                        stuLog.setAdapter(studentScheduleAdapter1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Error retrieving schedule.",Toast.LENGTH_SHORT).show();
            }
//            dialog.cancel();
        }
    }
}
