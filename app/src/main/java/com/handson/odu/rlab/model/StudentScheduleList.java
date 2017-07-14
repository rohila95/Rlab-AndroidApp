package com.handson.odu.rlab.model;

import java.util.List;

/**
 * Created by rgudipati on 3/14/2017.
 */

public class StudentScheduleList {
    private List<StudentSchedule> schedule_data;
    private List<StudentSchedule> availability_data;

    public List<StudentSchedule> getSchedule_data() {
        return schedule_data;
    }

    public void setSchedule_data(List<StudentSchedule> schedule_data) {
        this.schedule_data = schedule_data;
    }

    public List<StudentSchedule> getAvailability_data() {
        return availability_data;
    }

    public void setAvailability_data(List<StudentSchedule> availability_data) {
        this.availability_data = availability_data;
    }
}
