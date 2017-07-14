package com.handson.odu.rlab.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.ProjectAdapter;

import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.AvailStudents;
import com.handson.odu.rlab.model.AvailUser;
import com.handson.odu.rlab.model.ProjectsList;
import com.handson.odu.rlab.model.Student;
import com.handson.odu.rlab.model.StudentsProfessor;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AgilecardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AgilecardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgilecardsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//public List<Project> projectList=new ArrayList<Project>();
    Map members;
    Map professors;
    RadioGroup group;

    StudentsProfessor student;
    private OnFragmentInteractionListener mListener;

    public AgilecardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgilecardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgilecardsFragment newInstance(String param1, String param2) {
        AgilecardsFragment fragment = new AgilecardsFragment();
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
        View myInflatedView =inflater.inflate(R.layout.fragment_agilecards, container, false);
        members=new HashMap();
        professors=new HashMap();
        String[] str={};


        new RequestProjectDetails().execute();
        new RequestNewProjectDetails().execute();
        return myInflatedView;
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
    public class RequestProjectDetails extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading projects...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid", RLabApplication.getId());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetAgileBoardData.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from project service is "+result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    ProjectsList projectsList = mapper.readValue(result, ProjectsList.class);


                    ListView listView = (ListView) getView().findViewById(R.id.projList);
                    ProjectAdapter adapter = new ProjectAdapter(getActivity().getApplicationContext(), projectsList.getAgileboarddata());
                    listView.setAdapter(adapter);
                    listView.setDivider(null);
                    listView.setDividerHeight(0);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getActivity().getApplicationContext(),"clickkkk",Toast.LENGTH_SHORT).show();
//                        System.out.println("clickkkkkkkk");
//
//                    }
//                });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(getActivity().getApplicationContext(),"Error loading projects. Try again.",Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    }
    public class RequestNewProjectDetails extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading projects...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid",RLabApplication.getId());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/getUserDetails.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from student details service is "+result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    student = mapper.readValue(result, StudentsProfessor.class);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.addProj);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder addProj = new AlertDialog.Builder(getActivity());
                        addProj.setTitle("New Project");
                        LinearLayout layout = new LinearLayout(getActivity());
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setPadding(20, 20, 20, 20);

                        final EditText projName = new EditText(getActivity());
                        final TextView profName = new TextView(getActivity());
                        if (RLabApplication.getRole().equals("T.A")) {
                            projName.setHint("Course Name");
                            layout.addView(projName);

                            profName.setText("Professor");
                            layout.addView(profName);
                            group = new RadioGroup(getActivity());
                            group.setOrientation(RadioGroup.HORIZONTAL);
                            for (final AvailUser availStudents : student.getProfessors()) {
                                final RadioButton cb = new RadioButton(getActivity());
                                cb.setText(availStudents.getUsername());
//                            cb.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if(cb.isChecked())
//                                        professors.put(availStudents.getUserid(),availStudents.getUsername());
//                                    else
//                                        professors.remove(availStudents.getUserid());
//                                }
//                            });

                                group.addView(cb);
                            }

                            layout.addView(group);


                        } else {

                            projName.setHint("Project Name");
                            layout.addView(projName);
                        }


                        TextView mem = new TextView(getActivity());
                        mem.setText("  Member(s)");
                        layout.addView(mem);

                        for (final AvailUser availStudents : student.getStudents()) {
                            final CheckBox cb = new CheckBox(getActivity());
                            cb.setText(availStudents.getUsername());
                            cb.setId(Integer.parseInt(availStudents.getUserid()));
                            cb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (cb.isChecked())
                                        members.put(availStudents.getUserid(), availStudents.getUsername());
                                    else
                                        members.remove(availStudents.getUserid());

                                }
                            });

                            layout.addView(cb);
                        }
                        final EditText update = new EditText(getActivity());
                        update.setSingleLine(false);
                        update.setHint("Update");
                        layout.addView(update);
                        addProj.setView(layout);
                        addProj.setPositiveButton("Add Project", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (projName.getText().toString().equals("")) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Project name can't be null !", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (RLabApplication.getRole().equals("T.A")) {
                                        int radioButtonID = group.getCheckedRadioButtonId();

                                        int index = 0;
                                        if (radioButtonID == -1)
                                            Toast.makeText(getActivity().getApplicationContext(), "Select a professor ", Toast.LENGTH_SHORT).show();
                                        else {
                                            System.out.println("prof--" + radioButtonID + "---" + student.getProfessors().size());
                                            if (radioButtonID - 1 > student.getProfessors().size())
                                                index = (radioButtonID - 1) % student.getProfessors().size();
                                            else
                                                index = (radioButtonID - 1);
                                            System.out.println("prof--" + index);
                                            AvailUser prof = student.getProfessors().get(index);

                                            new AddProject().execute(projName.getText().toString(), update.getText().toString(), prof.getUserid(), members.toString());
                                        }
                                    } else {
                                        new AddProject().execute(projName.getText().toString(), update.getText().toString(), "2", members.toString());
                                    }
                                    System.out.println("members " + members.toString());


                                }
                            }
                        });
                        addProj.show();
                    }
                });
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Error creating new project.",Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        }
    }
    public class AddProject extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            dialog.setMessage("adding project...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid", RLabApplication.getId());
            map.put("message", params[1]);
            map.put("professorid",params[2]);
            map.put("project_name", params[0]);
            params[3] = params[3].substring(1, params[3].length()-1);
            String[] members=params[3].split(",");
            int i=0;
            for(String mem : members)
            {
                String[] entry = mem.split("=");
                map.put("students["+i+"]",entry[0].trim());
                i++;
            }
            System.out.println("data to query"+map.toString());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/CreateAgileBoardData.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from project service is "+result);
            if(!result.contains("Exception")) {

                Toast.makeText(getActivity().getApplicationContext(), "Project Added successfuly", Toast.LENGTH_SHORT).show();
                new RequestProjectDetails().execute();
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(), "Error in adding project.", Toast.LENGTH_SHORT).show();
            }
//            Intent intent = getActivity().getIntent();
//            getActivity().finish();
//            startActivity(intent);
            dialog.cancel();
        }
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
