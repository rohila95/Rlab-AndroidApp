package com.handson.odu.rlab.utility;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;

import com.handson.odu.rlab.UI.HomeActivity;
import com.handson.odu.rlab.application.RLabApplication;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rgudipati on 3/8/2017.
 */

public class BeaconService extends IntentService implements BeaconConsumer {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    int i=0;
    private BeaconManager beaconManager;
    int beaconflag=0,pingCount=0,count=-1;
    int beaconArray[];
    static String beaconStatus,statusId="0",availability;
    String personStatus=null;
    boolean beacon5=false;
    private BackgroundPowerSaver backgroundPowerSaver;
    public BeaconService() {
        super("BeaconService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        while(true)
//            System.out.println(i++);

        beaconManager=BeaconManager.getInstanceForApplication(this);

       // beaconManager=BeaconManager.getInstanceForApplication(this);

        //beaconManager.setBackgroundScanPeriod(1000);

        backgroundPowerSaver = new BackgroundPowerSaver(this);

        beaconManager.setBackgroundBetweenScanPeriod(2000);
        beaconManager.setForegroundBetweenScanPeriod(2000);
        beaconArray=new int[20];


        while(true)
            beaconManager.bind(this);

    }
    @Override
    public void onBeaconServiceConnect() {

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

                beacon5=false;
                for(Beacon beacon:collection) {

                    System.out.println("beacons are ----"+beacon.getId1()+"---"+beacon.getId2()+"---"+beacon.getId3());
                    if(beacon.getId3().toString().equals(RLabApplication.getBeacon_minor()) && beacon.getId1().toString().equals(RLabApplication.getBeacon_uuid()) && beacon.getId2().toString().equals(RLabApplication.getBeacon_major()) ) {
                        System.out.println("beaconnnnnnnnnnnnnnn");
                        beaconflag=1;
                        beacon5=true;
                    }
                }
                if(!beacon5){
                    beaconflag=0;
                    System.out.println("no beaconnnn");
                }

                count++;
                beaconArray[count]=beaconflag;

                if(count>=19)
                {
                    String prev_status=personStatus;
                    int sum=0;
                    for(int i=0;i<=count;i++)
                        sum+=beaconArray[i];
                    System.out.println("sum for 40 sec is...."+sum);
                    count=0;
                    beaconArray=new int[20];
                    if(sum>=13) {
                        System.out.println("available");
                        beaconStatus="insert";
                        personStatus="true";
                        availability="Yes";
                    }
                    else
                    {
                        System.out.println("outttttttttt");
                        beaconStatus="update";
                        personStatus="false";
                        availability="No";
                    }
                    System.out.println(prev_status+"---"+personStatus);
                    if(prev_status==null || !prev_status.equals(personStatus))
                        new SendStatus().execute(beaconStatus,availability);
                }

                for(int i=0;i<=count;i++)
                    System.out.println("beacon at "+i+"===="+beaconArray[i]);
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRegion",null,null,null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SendStatus extends AsyncTask<String,Integer,String>
    {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap hashMap=new HashMap<String,String>();
            hashMap.put("userid", RLabApplication.getId());
            hashMap.put("action",params[0]);
            hashMap.put("availability",params[1]);
            return RequestService.startService("http://qav2.cs.odu.edu/karan/LabBoard/AvailabilityLog.php",hashMap);
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println(" MY requests, Response from service beacon status send: " + result);
            if(result.startsWith("Inserted"))
                statusId= String.valueOf(Integer.parseInt(result.split(":")[1]));
            else if(result.startsWith("Update Successful"))
                statusId="0";

        }
    }
}
