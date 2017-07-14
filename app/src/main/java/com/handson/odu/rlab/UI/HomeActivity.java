package com.handson.odu.rlab.UI;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.handson.odu.rlab.Manifest;
import com.handson.odu.rlab.R;
import com.handson.odu.rlab.application.RLabApplication;
import com.handson.odu.rlab.utility.BeaconService;
import com.handson.odu.rlab.utility.RequestService;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity  {
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(),3);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Students");
        tabLayout.getTabAt(1).setText("Agile");
//        tabLayout.getTabAt(2).setText("Reminders");
        tabLayout.getTabAt(2).setText("Notes");
        System.out.println("location--"+isLocationEnabled(HomeActivity.this));
        if (!RLabApplication.getRole().equals("Professor"))
        {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
if(isLocationEnabled(HomeActivity.this)) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // Android M Permission check
        if (HomeActivity.this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @TargetApi(23)
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }

            });
            builder.show();
        }
    }
    Intent in = new Intent(this, BeaconService.class);
    startService(in);

}
            else
{
    final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
    builder.setTitle("Location not On");
    builder.setMessage("Please turn On your location for bluetooth beacon detection.");
    builder.setPositiveButton(android.R.string.ok, null);
    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {


        @Override
        public void onDismiss(DialogInterface dialog) {

        }

    });
    builder.show();
}


        }
    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "coarse location permission granted");
                    Intent in = new Intent(this, BeaconService.class);
                    startService(in);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.mainmenu, menu);
    MenuItem item = menu.findItem(R.id.lab_switch);
    item.setActionView(R.layout.switch_layout);

    final LinearLayout actionView = (LinearLayout) item.getActionView();
    Switch labSwitch= (Switch) actionView.findViewById(R.id.labSwitch);
    if(RLabApplication.getRole().equals("Professor")) {
        labSwitch.setVisibility(View.VISIBLE);
        if(RLabApplication.getId().equals("1"))
            labSwitch.setChecked(true);
    }
    else
        labSwitch.setVisibility(View.GONE);

    labSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // Start or stop your Service
            System.out.println("switch check...."+isChecked);
            if(isChecked) {
                RLabApplication.Tag = "RA";
                RLabApplication.setId("1");

            }
            else {
                RLabApplication.Tag = "TA";
                RLabApplication.setId("14");

            }
            finish();
            startActivity(getIntent());
        }
    });

    return true;
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
                ActivityCompat.finishAffinity(HomeActivity.this);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }



    class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position)
            {
                case 0: fragment=new AvailablestudentsFragment();
                    break;
                case 1: fragment=new AgilecardsFragment();
                    break;
//                case 2: fragment=new CalenderFragment();
//                    break;
                case 2: fragment=new MessageFragment();
                    break;
                default: break;
            }

            System.out.println("position fragment--"+position);

            return fragment;
        }

        @Override
        public int getCount() {
            return  mNumOfTabs;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
