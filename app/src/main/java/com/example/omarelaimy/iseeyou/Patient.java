package com.example.omarelaimy.iseeyou;

/**
 * Created by Omar on 6/20/2017.
 */

public class Patient {
    private String Name;
    private String Gender;
    private String ImagePath;
    private String Relation;

    public Patient()
    {
        //Empty constructor.
    }
    //Setting the attributes for a patient.
    public void SetPatientInfo(String name,String gender, String image,String relation)
    {
        this.Name = name;
        this.Gender = gender;
        this.ImagePath = image;
        this.Relation = relation;
    }
    public String GetName()
    {
        return this.Name;
    }
    public String GetGender()
    {
        return this.Gender;
    }
    public String GetImagePath()
    {
        return this.ImagePath;
    }
    public String GetRelation()
    {
        return this.Relation;
    }
}
