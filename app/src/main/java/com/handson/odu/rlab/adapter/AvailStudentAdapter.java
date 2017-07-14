package com.handson.odu.rlab.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handson.odu.rlab.R;
import com.handson.odu.rlab.model.AvailStudents;
import com.handson.odu.rlab.model.AvailUser;
import com.handson.odu.rlab.model.Note;

import java.io.InputStream;
import java.util.List;

/**
 * Created by rgudipati on 10/22/2016.
 */
public class AvailStudentAdapter extends BaseAdapter
{
    private final Context context;
    private final List<AvailStudents> availUserList;

    public AvailStudentAdapter(Context context, List<AvailStudents> availUserList)
    {
     this.context=context;
     this.availUserList=availUserList;
    }

    @Override
    public int getCount() {
        return availUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return availUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       System.out.println("in adapter"+position);
        AvailStudents avauser=availUserList.get(position);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View gridView=inflater.inflate(R.layout.student_list,null);

        ImageView img= (ImageView) gridView.findViewById(R.id.image);
       // img.setImageURI(Uri.parse("/storage/emulated/0/Android/data/com.handson.odu.rlab/pic1.png"));
//        img.setImageResource(R.drawable.profilepic);
//        img.setBackgroundColor(Color.RED);


        new DownloadImageTask(img).execute(avauser.getImage());

        if(avauser.getStatus().equals("Yes"))
            img.setBackgroundColor(context.getResources().getColor(R.color.available));

        return gridView;
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
