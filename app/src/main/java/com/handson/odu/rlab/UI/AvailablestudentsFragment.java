package com.handson.odu.rlab.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.StudentAdapter;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.Student;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvailablestudentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailablestudentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailablestudentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Student studentList;

File file;
    FileOutputStream out = null;
    private OnFragmentInteractionListener mListener;

    public AvailablestudentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailablestudentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailablestudentsFragment newInstance(String param1, String param2) {
        AvailablestudentsFragment fragment = new AvailablestudentsFragment();
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
        View myInflatedView =inflater.inflate(R.layout.fragment_availablestudents, container, false);
        new RequestStudentDetails().execute();
    //    studentList=new Student();
  //      availStudentsList=new ArrayList<AvailStudents>();
//studentList.clear();
//        String[] str={"chkd","recruitment system"};
//for(int i=0;i<9;i++)
//{
//    Student.AvailStudents stu=new Student.AvailStudents();
//    stu.setUsername("Maheedhar");
//    stu.setImage("/storage/emulated/0/Android/data/com.handson.odu.rlab/pic1.png");
//    stu.setUserid(1);
//    stu.setProjects("CHKD,Recruitment");
//    stu.setStatus("Yes");
//    availStudentsList.add(stu);
//}
//        studentList.setStudent_details(availStudentsList);
//        ListView listView= (ListView) myInflatedView.findViewById(R.id.studentList);
//        StudentAdapter adapter=new StudentAdapter(getActivity().getApplicationContext(),availStudentsList);
//        listView.setAdapter(adapter);
//        listView.setDivider(null);
//        listView.setDividerHeight(5);



        return myInflatedView;
    }
    public class RequestStudentDetails extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading students...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid",RLabApplication.getId());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/ChartData.php",map);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("Result from Student service is "+result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();

                try {

                    studentList = mapper.readValue(result, Student.class);
                    GridView listView = (GridView) getView().findViewById(R.id.studentList);
//                System.out.println("listttttttttttttttttttt"+studentList.getStudent_details());
                    RLabApplication.allStudents = studentList.getStudent_details();
                    StudentAdapter adapter = new StudentAdapter(getActivity().getApplicationContext(), studentList.getStudent_details());
                    listView.setAdapter(adapter);
//                listView.setDivider(null);
//                listView.setDividerHeight(5);
                    adapter.notifyDataSetChanged();


                    //   new DownloadImageTask().execute("http://qav2.cs.odu.edu/karan/LabBoard/Images/1.jpg");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getActivity().getApplicationContext(),"Error loading students.",Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
        }
    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // ImageView bmImage;

        public DownloadImageTask() {
            // this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                file = new File(Environment.getExternalStorageDirectory()+"/Android/data/com.handson.odu.rlab/pic1.png");
                System.out.println(Environment.getExternalStorageDirectory()+"/Android/data/com.handson.odu.rlab/pic1.png");
                FileOutputStream outputStream = new FileOutputStream(file);
                mIcon11.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            System.out.println("downloaded image");


        }
    }

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
