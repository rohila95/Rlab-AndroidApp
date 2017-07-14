package com.handson.odu.rlab.model;

/**
 * Created by rgudipati on 1/6/2017.
 */

public class Project {

        private String project_name;
        private String professor_name;
        private String username;
        private String message;
        private String time_posted;
        private String[] students;

    public String getProfessor_name() {
        return professor_name;
    }

    public void setProfessor_name(String professor_name) {
        this.professor_name = professor_name;
    }

    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime_posted() {
            return time_posted;
        }

        public void setTime_posted(String time_posted) {
            this.time_posted = time_posted;
        }

        public String[] getStudents() {
            return students;
        }

        public void setStudents(String[] students) {
            this.students = students;
        }

        public String getProject_name() {
            return project_name;
        }

        public void setProject_name(String project_name) {
            this.project_name = project_name;
        }



}
