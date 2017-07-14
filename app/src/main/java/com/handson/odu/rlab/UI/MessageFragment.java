package com.handson.odu.rlab.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.NoteAdapter;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.Note;
import com.handson.odu.rlab.model.NoteList;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static final List<Note> noteList=new ArrayList<Note>();
    NoteAdapter adapter;
GridView gridView;
    private OnFragmentInteractionListener mListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView =inflater.inflate(R.layout.fragment_message, container, false);
        noteList.clear();

//        for (int i=0;i<15;i++)
//        {
//            Note note=new Note();
//            note.setMessage_title("CHKD");
//            note.setMessage_description("Monday Meeting");
//            noteList.add(note);
//        }
//        System.out.println(noteList.toString());
        gridView = (GridView) myInflatedView.findViewById(R.id.messageGrid);
//        NoteAdapter adapter=new NoteAdapter(getActivity().getApplicationContext(),noteList);
//        gridView.setAdapter(adapter);
        new RequestNoteData().execute();
        FloatingActionButton fab= (FloatingActionButton) myInflatedView.findViewById(R.id.addNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder note=new AlertDialog.Builder(getActivity());
                note.setTitle("New Note");
                //note.setMessage("Enter note Details");

                LinearLayout layout=new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(20,20,20,20);

                final EditText noteTitle=new EditText(getActivity());
                noteTitle.setHint("Title");
                layout.addView(noteTitle);
                final EditText noteMessage=new EditText(getActivity());
                noteMessage.setHint("Message");
               // noteMessage.setPadding(5,5,5,5);

                layout.addView(noteMessage);
                note.setView(layout);
                note.setPositiveButton("Add Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Toast.makeText(getActivity().getApplicationContext(),noteTitle.getText().toString()+"-"+noteMessage.getText().toString()+"-"+ RLabApplication.getId(),Toast.LENGTH_SHORT).show();
                        new AddNote().execute(RLabApplication.getId(),noteTitle.getText().toString(),noteMessage.getText().toString());
                    }
                });
                note.show();
            }
        });

        return myInflatedView;
    }

    public class RequestNoteData  extends AsyncTask<String,Integer,String> {
        public ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading notes...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid",RLabApplication.getId());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetNotes.php",map);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Note service is " + result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    NoteList note = mapper.readValue(result, NoteList.class);
                    //System.out.println("note data"+note.getMessage_title());
                    gridView = (GridView) getView().findViewById(R.id.messageGrid);
                    adapter = new NoteAdapter(getActivity().getApplicationContext(), note.getNotes_data(), gridView);
                    gridView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Error getting notes.",Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        }
    }

    public class AddNote  extends AsyncTask<String,Integer,String> {
        public ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Adding notes...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid",params[0]);
            map.put("title",params[1]);
            map.put("description",params[2]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/CreateNotes.php",map);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from add Note service is " + result);
            if(!result.contains("Exception")) {
                Toast.makeText(getActivity().getApplicationContext(), "Note added successfully.", Toast.LENGTH_SHORT).show();
                new RequestNoteData().execute();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "Error adding note.", Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();


        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
