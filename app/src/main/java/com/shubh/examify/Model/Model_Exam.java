package com.shubh.examify.Model;

public class Model_Exam {

    String Date ,Instructions ,Subject , Title ,PDFurl, img , Name , Chapter , isActive , examinarID , MaxMarks;
    int finHour , inHour , finMin , inMin;


    public Model_Exam() {
    }

    String Key;

    public Model_Exam(String key) {
        Key = key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Model_Exam(String Date, String Instructions, String Subject, String Title, String MaxMarks, String isActive, String PDFurl, String examinarID, String img, String Name, String Chapter, int finHour, int inHour, int finMin, int inMin) {
        this.Date = Date;
        this.Instructions = Instructions;
        this.Subject = Subject;
        this.Title = Title;
        this.PDFurl = PDFurl;
        this.img = img;
        this.finHour = finHour;
        this.inHour = inHour;
        this.finMin = finMin;
        this.inMin = inMin;
        this.Name = Name;
        this.Chapter = Chapter;
        this.isActive = isActive;
        this.examinarID = examinarID;
        this.MaxMarks = MaxMarks;
    }


    public String getExaminarID() {
        return examinarID;
    }

    public void setExaminarID(String examinarID) {
        this.examinarID = examinarID;
    }

    public String getMaxMarks() {
        return MaxMarks;
    }

    public void setMaxMarks(String maxMarks) {
        MaxMarks = maxMarks;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getChapter() {
        return Chapter;
    }

    public void setChapter(String chapter) {
        Chapter = chapter;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPDFurl() {
        return PDFurl;
    }

    public void setPDFurl(String URL) {
        this.PDFurl = PDFurl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getFinHour() {
        return finHour;
    }

    public void setFinHour(int finHour) {
        this.finHour = finHour;
    }

    public int getInHour() {
        return inHour;
    }

    public void setInHour(int inHour) {
        this.inHour = inHour;
    }

    public int getFinMin() {
        return finMin;
    }

    public void setFinMin(int finMin) {
        this.finMin = finMin;
    }

    public int getInMin() {
        return inMin;
    }

    public void setInMin(int inMin) {
        this.inMin = inMin;
    }
}
