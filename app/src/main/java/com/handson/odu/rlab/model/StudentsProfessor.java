package com.handson.odu.rlab.model;

import java.util.List;

/**
 * Created by rgudipati on 5/22/2017.
 */

public class StudentsProfessor {
    List<AvailUser> students;
    List<AvailUser> professors;

    public List<AvailUser> getStudents() {
        return students;
    }

    public void setStudents(List<AvailUser> students) {
        this.students = students;
    }

    public List<AvailUser> getProfessors() {
        return professors;
    }

    public void setProfessors(List<AvailUser> professors) {
        this.professors = professors;
    }
}
