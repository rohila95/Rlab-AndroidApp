package com.handson.odu.rlab.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.adapter.EventAdapter;
import com.handson.odu.rlab.adapter.StudentAdapter;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.Event;
import com.handson.odu.rlab.model.EventList;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalenderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
String edate,estime,eetime;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
public List<Event> eventList=new ArrayList<Event>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private OnFragmentInteractionListener mListener;

    public CalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
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
                             final Bundle savedInstanceState) {
        View myInflatedView=inflater.inflate(R.layout.fragment_calender, container, false);
        // Inflate the layout for this fragment
        new RequestEventDetails().execute();

        FloatingActionButton fab= (FloatingActionButton) myInflatedView.findViewById(R.id.addEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder event=new AlertDialog.Builder(getActivity());
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

//                final TextView date=new TextView(getActivity());
//                date.setHint("  Date");
//                date.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                //        Toast.makeText(getActivity().getApplicationContext(),"time",Toast.LENGTH_SHORT).show();
//
//                        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                              //  Toast.makeText(getActivity().getApplicationContext(),dayOfMonth+"-"+monthOfYear+"-"+year,Toast.LENGTH_SHORT).show();
//                                date.setText("  Date : "+(monthOfYear+1)+"-"+dayOfMonth+"-"+year);
//                                edate=(monthOfYear+1)+"-"+dayOfMonth+"-"+year;
//                            }
//                        }, mYear, mMonth, mDay);
//                        datePickerDialog.show();
//                    }
//                });
//                final TextView fromTime=new TextView(getActivity());
//                fromTime.setHint("  From Time");
//                fromTime.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                fromTime.setText("  From : "+hourOfDay+" : "+minute);
//                                estime=hourOfDay+" : "+minute;
//                            }
//
//                        },mHour,mMinute,false);
//                        timePickerDialog.show();
//                    }
//                });
//
//                final TextView toTime=new TextView(getActivity());
//                toTime.setHint("  To Time");
//                toTime.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                toTime.setText("  To : "+hourOfDay+" : "+minute);
//                                eetime=hourOfDay+" : "+minute;
//                            }
//
//                        },mHour,mMinute,false);
//                        timePickerDialog.show();
//                    }
//                });
//                layout.addView(date);
//                layout.addView(fromTime);
//                layout.addView(toTime);
//                event.setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity().getApplicationContext(),eventName.getText().toString()+edate+estime+eetime,Toast.LENGTH_SHORT).show();
//                    }
//                });
//                event.setView(layout);
                View addevent=getLayoutInflater(savedInstanceState).inflate(R.layout.add_event,null);
                final LinearLayout linearLayout= (LinearLayout) addevent.findViewById(R.id.days);
                final TextView date= (TextView) addevent.findViewById(R.id.selectDate);
                final RadioButton once= (RadioButton) addevent.findViewById(R.id.once);
                final RadioButton week= (RadioButton) addevent.findViewById(R.id.week);
                final RadioButton day= (RadioButton) addevent.findViewById(R.id.day);
                final TextView fromTime= (TextView) addevent.findViewById(R.id.fromTime);
                final TextView toTime= (TextView) addevent.findViewById(R.id.toTime);
                final ImageView calImg= (ImageView) addevent.findViewById(R.id.calImg);
                toTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                toTime.setText("  To : "+hourOfDay+" : "+minute);
                                eetime=hourOfDay+" : "+minute;
                            }

                        },mHour,mMinute,false);
                        timePickerDialog.show();
                    }
                });
                fromTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                fromTime.setText("  From : "+hourOfDay+" : "+minute);
                                estime=hourOfDay+" : "+minute;
                            }

                        },mHour,mMinute,false);
                        timePickerDialog.show();
                    }
                });
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                              //  Toast.makeText(getActivity().getApplicationContext(),dayOfMonth+"-"+monthOfYear+"-"+year,Toast.LENGTH_SHORT).show();
                                date.setText("  Date : "+(monthOfYear+1)+"-"+dayOfMonth+"-"+year);
                                edate=(monthOfYear+1)+"-"+dayOfMonth+"-"+year;
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }

                });
                once.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(once.isChecked())
                        {
                            linearLayout.setVisibility(View.GONE);
                            date.setVisibility(View.VISIBLE);
                            calImg.setVisibility(View.VISIBLE);
                        }
                    }
                });
                week.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(week.isChecked())
                        {
                            linearLayout.setVisibility(View.VISIBLE);
                            date.setVisibility(View.GONE);
                            calImg.setVisibility(View.GONE);
                        }
                    }
                });
                day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(day.isChecked())
                        {
                            linearLayout.setVisibility(View.GONE);
                            date.setVisibility(View.GONE);
                            calImg.setVisibility(View.GONE);
                        }
                    }
                });

                event.setView(addevent);
                event.show();

            }
        });
        return myInflatedView;
    }

    public class RequestEventDetails extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading events...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap map=new HashMap<String,String>();
            map.put("userid", RLabApplication.getId());
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/GetEventDetails.php",map);
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
                    ListView listView = (ListView) getView().findViewById(R.id.events);
                    EventAdapter adapter = new EventAdapter(getActivity().getApplicationContext(), eventList.getEvent_details());
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
