package com.handson.odu.rlab.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.AvailStudentAdapter;
import com.handson.odu.rlab.adapter.EventAdapter;
import com.handson.odu.rlab.adapter.NoteAdapter;
import com.handson.odu.rlab.adapter.StudentAdapter;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.AvailUser;
import com.handson.odu.rlab.model.Event;
import com.handson.odu.rlab.model.EventList;
import com.handson.odu.rlab.model.Note;
import com.handson.odu.rlab.model.NoteList;
import com.handson.odu.rlab.model.Student;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DisplayboardActivity extends Activity implements TextToSpeech.OnInitListener{
GridView gridView,avaGrid;
    Student studentList;
    static final List<Note> noteList=new ArrayList<Note>();
    TextToSpeech textToSpeech;
    File file;
    static final List<AvailUser> stuList=new ArrayList<AvailUser>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayboard);

        textToSpeech=new TextToSpeech(getApplicationContext(), this);

        noteList.clear();
        stuList.clear();
//            for (int i=0;i<15;i++)
//            {
//                    Note note=new Note();
//                    note.setMessage_title("CHKD");
//                note.setMessage_description("Monday Meeting");
//                //note.setMessage_time("");
//                    noteList.add(note);
//            }
        System.out.println(noteList.toString());

        new RequestNoteData().execute();
        new RequestStudentDetails().execute();
        new RequestEventDetails().execute();





//        for(int i=0;i<10;i++)
//        {
//            Event event=new Event();
//            event.setEvent_title("College of Sciences training");
//            event.setEvent_message("training to explain new functionalities");
//            event.setEvent_start_time("2016-10-23 19:55:51");
//            event.setEvent_end_time("2016-10-28 17:00:00");
//            eventList.add(event);
//        }
        avaGrid= (GridView) findViewById(R.id.AvailGrid);
        avaGrid.setNumColumns(9);

//        AvailStudentAdapter stuAdapter=new AvailStudentAdapter(getApplicationContext(),stuList);
//        avaGrid.setAdapter(stuAdapter);
        VideoView videoView= (VideoView) findViewById(R.id.videoView);
        //videoView.setVideoURI(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"));
        MediaController mediaController= new MediaController(this);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);

//        ListView reminders= (ListView) findViewById(R.id.reminders);
//        EventAdapter eventAdapter=new EventAdapter(getApplicationContext(),eventList);
//        reminders.setAdapter(eventAdapter);
//        reminders.setDivider(null);
//        reminders.setDividerHeight(20);
        System.out.println("Env"+Environment.getExternalStorageDirectory().getPath());

    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
            String text="Welcome to lab.";
           // textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class RequestNoteData  extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(DisplayboardActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Signing In...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetNotes.php",null);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Note service is "+result);
            ObjectMapper mapper=new ObjectMapper();
            try {
                NoteList note=mapper.readValue(result,NoteList.class);
                //System.out.println("note data"+note.getMessage_title());
                gridView = (GridView) findViewById(R.id.notesGrid);
                NoteAdapter adapter=new NoteAdapter(getApplicationContext(),note.getNotes_data(),gridView);
                gridView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to exit the app? ");
        builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.finishAffinity(DisplayboardActivity.this);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }
    public class RequestStudentDetails extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(DisplayboardActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading students...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetStudentDetails.php",null);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Student service is "+result);
            ObjectMapper mapper=new ObjectMapper();

            try
            {
                studentList=mapper.readValue(result,Student.class);
                GridView listView= (GridView) findViewById(R.id.AvailGrid);
                System.out.println("listttttttttttttttttttt"+studentList.getStudent_details());
                RLabApplication.allStudents=studentList.getStudent_details();
                AvailStudentAdapter adapter=new AvailStudentAdapter(getApplicationContext(),studentList.getStudent_details());
                listView.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            dialog.cancel();
        }
    }

    public class RequestEventDetails extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(DisplayboardActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Signing In...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetClassDetails.php",null);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Event service is "+result);
            ObjectMapper mapper=new ObjectMapper();
            try {
                EventList eventList=mapper.readValue(result,EventList.class);
                if(eventList.getEvent_details()==null)
                {
                    System.out.println("no eventsss");
                }
                else {
                    ListView listView = (ListView) findViewById(R.id.reminders);
                    listView.setDivider(null);
                    listView.setDividerHeight(20);
                    EventAdapter adapter = new EventAdapter(getApplicationContext(), eventList.getEvent_details());
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    listView.setDividerHeight(20);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            dialog.cancel();
        }
    }

}
