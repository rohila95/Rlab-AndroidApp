package com.handson.odu.rlab.UI;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.iid.FirebaseInstanceId;
import com.handson.odu.rlab.Manifest;
import com.handson.odu.rlab.R;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.model.User;
import com.handson.odu.rlab.utility.RequestService;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText userName,password;
    HashMap<String,String> hashMap;
    Intent in;
    User user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String fireBaseId;
    Switch rememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         rememberMe= (Switch) findViewById(R.id.mySwitch);
        userName= (EditText) findViewById(R.id.userName);
        password= (EditText) findViewById(R.id.password);
        sharedPreferences=getPreferences(Context.MODE_PRIVATE);
        System.out.println("Firebase Instance Id: "+ FirebaseInstanceId.getInstance().getToken());
        fireBaseId=FirebaseInstanceId.getInstance().getToken();
        login= (Button) findViewById(R.id.login);

        System.out.println("network state--"+isNetworkAvailable());
        if(!sharedPreferences.getString("Id","0").equals("0") && !sharedPreferences.getString("password","0").equals("0"))
        {
            System.out.println(sharedPreferences.getString("Id","0")+"--"+sharedPreferences.getString("password","0"));
            userName.setText(sharedPreferences.getString("Id","0"));
            password.setText(sharedPreferences.getString("password","0"));

            rememberMe.setChecked(true);
//            new RequestLogin().execute(sharedPreferences.getString("Id","0"),sharedPreferences.getString("password","0"));
        }



        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    userName.setText("");
                    password.setText("");
                }
            }
        });
        sharedPreferences=getPreferences(Context.MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Android M Permission check
                        if (LoginActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("This app needs storage access");
                            builder.setMessage("Please grant storage access so this app can store your details in local.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            1);
                                }

                            });
                            builder.show();
                        } else {
                            if (rememberMe.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Id", userName.getText().toString());
                                editor.putString("password", password.getText().toString());
                                editor.putString("rememberOn", "true");
                                editor.commit();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Id", "0");
                                editor.putString("password", "0");
                                editor.putString("rememberOn", "true");
                                editor.commit();
                            }
                            new RequestLogin().execute(userName.getText().toString(), password.getText().toString(), fireBaseId);
                        }
                    }

                }
                else
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("No Internet");
                    builder.setMessage("Please turn On your mobile data or wifi to login.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {


                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }

                    });
                    builder.show();
                }



            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tag", "coarse location permission granted");
                    System.out.println(userName.getText().toString()+"------------"+password.getText().toString());
                    if(rememberMe.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Id", userName.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.putString("rememberOn","true");
                        editor.commit();
                    }
                    else
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Id", "0");
                        editor.putString("password", "0");
                        editor.putString("rememberOn","true");
                        editor.commit();
                    }

                    new RequestLogin().execute(userName.getText().toString(),password.getText().toString(),fireBaseId);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since storage access has not been granted, this app will not be able to store your details.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            new RequestLogin().execute(userName.getText().toString(),password.getText().toString(),fireBaseId);
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }
    class RequestLogin extends AsyncTask<String,Integer,String>
    {
        public ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Signing In...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(params[2]==null)
                params[2]="0";
            System.out.println(params[0]+"----"+params[1]+"----"+params[2]);
            hashMap=new HashMap<String,String>();
            hashMap.put("username",params[0]);
            hashMap.put("password",params[1]);
            hashMap.put("deviceid",params[2]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/login.php",hashMap);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println(" MY requests, Response from service: " + result);
            if(!result.contains("Exception")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    user = mapper.readValue(result, User.class);
                    sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("userId", user.getUserid());
                    editor.commit();

                    if (user.getRole().equals("Professor")) {
                        RLabApplication.setId("14");
                        RLabApplication.Tag = "TA";
                    } else {
                        RLabApplication.setId(user.getUserid());


                    }
                    RLabApplication.setRole(user.getRole());
                    RLabApplication.setName(user.getUsername());
                    RLabApplication.setBeacon_major(user.getBeacon_major());
                    RLabApplication.setBeacon_minor(user.getBeacon_minor());
                    RLabApplication.setBeacon_uuid(user.getBeacon_uuid());

//                if(user.getRole().equals("T.A") || user.getRole().equals("R.A")) {
                    in = new Intent(getApplicationContext(), HomeActivity.class);
//                }
//                else
//                {
//                    in = new Intent(getApplicationContext(),DisplayboardActivity.class);
//
//                }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //in = new Intent(getApplicationContext(),DisplayboardActivity.class);
                //in=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(in);
            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                // set title
                alertDialogBuilder.setTitle("Error");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Invalid username/password. Try Again!")
                        .setCancelable(false)

                        .setNeutralButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
            dialog.cancel();
        }
    }
}
