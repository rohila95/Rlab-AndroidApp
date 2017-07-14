package com.handson.odu.rlab.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.UI.HomeActivity;
import com.handson.odu.rlab.UI.MessageFragment;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.Note;
import com.handson.odu.rlab.model.NoteList;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rgudipati on 10/22/2016.
 */
public class NoteAdapter extends BaseAdapter {
    private final Context context;
    private final List<Note> noteList;
    GridView gridView;
    Note note;

    public NoteAdapter(Context context, List<Note> noteList,GridView gridView)
    {
        this.context=context;
        this.noteList=noteList;
        this.gridView=gridView;
    }
    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    //    System.out.println("in adapter"+position);
         note=noteList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView=inflater.inflate(R.layout.note_list,null);
        TextView noteTitle= (TextView) gridView.findViewById(R.id.noteTitle);
        noteTitle.setText(note.getTitle());
        final ImageView img= (ImageView) gridView.findViewById(R.id.delete);
        img.setId(Integer.parseInt(note.getId()));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
////                System.out.println("deleteeeee");
//                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
//                builder.setTitle("Alert");
//                builder.setMessage("Do you want to delete this note? ");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        new DeleteNote().execute(img.getId()+"");
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.show();
                new DeleteNote().execute(img.getId()+"");
            }
        });
        TextView noteDesc= (TextView) gridView.findViewById(R.id.noteDesc);
        noteDesc.setText(note.getDescription());

        TextView noteDate= (TextView) gridView.findViewById(R.id.noteDate);
        //noteDate.setText(note.getMessage_time().toString());

        return gridView;
    }
    public class DeleteNote  extends AsyncTask<String,Integer,String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("id",params[0]);
            System.out.println("deleteddd id=="+params[0]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/DeleteNotes.php",map);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from delete Note service is " + result);
            Toast.makeText(context,"Note Removed",Toast.LENGTH_SHORT).show();
            new RequestNoteData().execute();
        }
    }

    public class RequestNoteData  extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid", RLabApplication.getId());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetNotes.php",map);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Note service is " + result);
            ObjectMapper mapper=new ObjectMapper();
            try {
                NoteList note=mapper.readValue(result,NoteList.class);
                gridView.setAdapter(new NoteAdapter(context,note.getNotes_data(),gridView));
                //System.out.println("note data"+note.getMessage_title());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
