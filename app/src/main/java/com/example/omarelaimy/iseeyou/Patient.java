package com.example.omarelaimy.iseeyou;

/**
 * Created by Omar on 6/20/2017.
 */

public class Patient {
    private String Name;
    private String ID;
    private String Gender;
    private String ImagePath;
    private String Relation;
    private String ProductID;
    private double CurrentHeartRate;
    private double AverageHeartRate;
    private String CurrentHeartRateTimeStamp;
    private String AverageHeartRateTimeStamp;

    public Patient()
    {
        //Empty constructor.
    }
    //Setting the attributes for a patient.
    public void SetPatientInfo(String name,String id,String gender, String image,String relation,String productid)
    {
        this.Name = name;
        this.ID = id;
        this.Gender = gender;
        this.ImagePath = image;
        this.Relation = relation;
        this.ProductID = productid;
    }
    public void SetCurrentHeartRate(double currentheartrate,String timestamp)
    {
        this.CurrentHeartRate = currentheartrate;
        this.CurrentHeartRateTimeStamp = timestamp;
    }
    public void SetAverageHeartRate(double averageheartrate)
    {
        this.AverageHeartRate = averageheartrate;
    }
    public String GetCurrentHeartRate()
    {
        return Double.toString(this.CurrentHeartRate);
    }
    public String GetAverageHeartRate()
    {
        return Double.toString(this.AverageHeartRate);
    }
    public String GetName()
    {
        return this.Name;
    }
    public String GetID() {return this.ID;}
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
    public String GetProductID()
    {
        return this.ProductID;
    }
}
