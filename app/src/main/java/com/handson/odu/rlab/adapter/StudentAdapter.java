package com.handson.odu.rlab.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.firebase.client.collection.LLRBNode;
import com.handson.odu.rlab.R;
import com.handson.odu.rlab.UI.StudentlogActivity;
import com.handson.odu.rlab.model.AvailStudents;
import com.handson.odu.rlab.model.AvailUser;
import com.handson.odu.rlab.model.Student;
import com.handson.odu.rlab.utility.RequestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rgudipati on 10/24/2016.
 */
public class StudentAdapter extends BaseAdapter {
    private Context context;
    private List<AvailStudents> studentList;
String[] xlabels;

    public StudentAdapter(Context context,List<AvailStudents> studentList)
    {
        this.context=context;
        this.studentList=studentList;
    }
    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AvailStudents student=studentList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView=inflater.inflate(R.layout.availablestu_list,null);
       // listView.setBackgroundColor(Color.RED);

       // img.setImageResource(R.drawable.profilepic);
   //     img.setImageURI(Uri.parse("/storage/emulated/0/Android/data/com.handson.odu.rlab/pic1.png"));
   //     img.setImageResource(R.drawable.alarm);
//        System.out.println("graph data is..."+student.getValue().toString()+"---"+student.getValue().toString());
        XYPlot plot=(XYPlot)listView.findViewById(R.id.xyPlot);
        List s1 = getSeries(student.getValue());
        xlabels=student.getxLabels();
        XYSeries series1 = new SimpleXYSeries(s1,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Time spent");
        LineAndPointFormatter s1Format = new LineAndPointFormatter();
        s1Format.setPointLabelFormatter(new PointLabelFormatter());
        s1Format.configure(context,R.xml.lpf1);
        plot.addSeries(series1, s1Format);
        plot.setTicksPerRangeLabel(1);
        plot.getGraphWidget().setDomainLabelOrientation(-45);

        plot.getGraphWidget().getDomainOriginTickLabelPaint().setTextSize(15);
        plot.getGraphWidget().getRangeOriginTickLabelPaint().setTextSize(15);
        plot.getGraphWidget().getDomainTickLabelPaint().setTextSize(15);
        plot.getGraphWidget().getRangeTickLabelPaint().setTextSize(15);
        plot.getGraphWidget().setDomainValueFormat(new GraphXLabelFormat());



//        ImageView img= (ImageView) listView.findViewById(R.id.stuimage);
//        new DownloadImageTask(img).execute(student.getMobile_image());
        TextView name= (TextView) listView.findViewById(R.id.stuName);
        name.setText(student.getUsername());
        TextView projects= (TextView) listView.findViewById(R.id.ProjName);
        projects.setText(student.getProjects());
        TextView colorStatus= (TextView) listView.findViewById(R.id.colorStatus);
//        LinearLayout nameSpace= (LinearLayout) listView.findViewById(R.id.nameSpace);
        if(student.getStatus().equals("Yes"))
//            colorStatus.setBackgroundColor(context.getResources().getColor(R.color.available));
        colorStatus.setBackgroundColor(Color.GREEN);
        else
            colorStatus.setBackgroundColor(Color.RED);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"On studenttttt "+ student.getUserid(),Toast.LENGTH_SHORT).show();
                Intent in=new Intent(context,StudentlogActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("id", ""+student.getUserid());
                context.startActivity(in);
            }
        });
        return listView;
    }
    private List getSeries(int[] values) {
        List series = new ArrayList();
        for(int i=0;i<values.length;i++)
            series.add(values[i]);

        return series;
    }
    private class GraphXLabelFormat extends Format {

        @Override
        public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
            int parsedInt = Math.round(Float.parseFloat(object.toString()));
            System.out.println("parsedInt  "+parsedInt);
//            String label=range.get(parsedInt).getRangeStart()+"-"+range.get(parsedInt).getRangeEnd();
            //range.get(parsedInt).getRangeStart()+"-"+
            switch(parsedInt) {
                case 0: buffer.append(xlabels[0]); break;
                case 1: buffer.append(xlabels[1]);break;
                case 2: buffer.append(xlabels[2]);break;
                case 3: buffer.append(xlabels[3]);break;
                case 4: buffer.append(xlabels[4]);break;
                case 5: buffer.append(xlabels[5]);break;
                case 6: buffer.append(xlabels[6]);break;
            }

            return buffer;
        }

        @Override
        public Object parseObject(String string, ParsePosition position) {
            //    return java.util.Arrays.asList(xLabels).indexOf(string);
            return null;
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public DownloadImageTask(ImageView image) {
             this.image = image;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                System.out.println("1---------"+urldisplay);
               // mIcon11.compress(Bitmap.CompressFormat.PNG, 100);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
            System.out.println("downloaded image ");


        }
    }
}
