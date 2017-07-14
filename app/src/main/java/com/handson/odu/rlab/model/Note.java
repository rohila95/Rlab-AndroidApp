package com.handson.odu.rlab.model;

import java.sql.Timestamp;

/**
 * Created by rgudipati on 10/22/2016.
 */
public class Note {
    String userid;
    String id;
    String title;
    String description;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
