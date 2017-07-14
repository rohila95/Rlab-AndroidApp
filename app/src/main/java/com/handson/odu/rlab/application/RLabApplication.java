package com.handson.odu.rlab.application;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.handson.odu.rlab.model.AvailStudents;
import com.handson.odu.rlab.model.Student;

import java.util.List;

/**
 * Created by rgudipati on 10/24/2016.
 */
public class RLabApplication extends Application {
    public static List<AvailStudents> allStudents;
    public static String id;
    public static String name;
    public static String role;
    public static String Tag;
    public static String beacon_major;
    public static String beacon_minor;
    public static String beacon_uuid;

    public static String getBeacon_major() {
        return beacon_major;
    }

    public static void setBeacon_major(String beacon_major) {
        RLabApplication.beacon_major = beacon_major;
    }

    public static String getBeacon_minor() {
        return beacon_minor;
    }

    public static void setBeacon_minor(String beacon_minor) {
        RLabApplication.beacon_minor = beacon_minor;
    }

    public static String getBeacon_uuid() {
        return beacon_uuid;
    }

    public static void setBeacon_uuid(String beacon_uuid) {
        RLabApplication.beacon_uuid = beacon_uuid;
    }

    public static List<AvailStudents> getAllStudents() {
        return allStudents;
    }

    public static void setAllStudents(List<AvailStudents> allStudents) {
        RLabApplication.allStudents = allStudents;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        RLabApplication.id = id;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        RLabApplication.role = role;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        RLabApplication.name = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing firebase
        Firebase.setAndroidContext(getApplicationContext());
        System.out.println("Firebase Instance Id: "+ FirebaseInstanceId.getInstance().getToken());
    }
}
