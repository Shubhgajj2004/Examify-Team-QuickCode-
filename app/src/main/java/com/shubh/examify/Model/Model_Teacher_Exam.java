package com.shubh.examify.Model;

public class Model_Teacher_Exam {

    String studentID, Marks, studentNAME, Ans;
    String Key;

    public Model_Teacher_Exam() {
        //Required for Firebase Database
    }

    public Model_Teacher_Exam(String key) {
        Key = key;
    }

    public Model_Teacher_Exam(String studentID, String marks, String studentNAME, String Ans) {
        this.studentID = studentID;
        Marks = marks;
        this.studentNAME = studentNAME;
        this.Ans = Ans;
    }


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getMarks() {
        return Marks;
    }

    public void setMarks(String marks) {
        Marks = marks;
    }

    public String getStudentNAME() {
        return studentNAME;
    }

    public void setStudentNAME(String studentNAME) {
        this.studentNAME = studentNAME;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }
}
