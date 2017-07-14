package com.handson.odu.rlab.model;

/**
 * Created by rgudipati on 10/22/2016.
 */
public class User {
    private String userid;
    private String username;
    private String image;
    private String role;
    private String status;
    private String beacon_major;
    private String beacon_minor;
    private String beacon_uuid;

    public String getBeacon_major() {
        return beacon_major;
    }

    public void setBeacon_major(String beacon_major) {
        this.beacon_major = beacon_major;
    }

    public String getBeacon_minor() {
        return beacon_minor;
    }

    public void setBeacon_minor(String beacon_minor) {
        this.beacon_minor = beacon_minor;
    }

    public String getBeacon_uuid() {
        return beacon_uuid;
    }

    public void setBeacon_uuid(String beacon_uuid) {
        this.beacon_uuid = beacon_uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setRole(String role) {
        this.role = role;
    }
}
