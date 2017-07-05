package com.example.omarelaimy.iseeyou;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hp on 01-Jul-17.
 */

public class Pill_Inventory {
    private String PillName;
    private int PillType;
    private int PillCount;
    //int Position;
  private LinearLayout MainLayout;


    public Pill_Inventory()
    {
        //Empty constructor.
    }
    //Setting the attributes for a patient.
    public void SetPillInventory(String pillname,int pilltype, int pillcount, LinearLayout mainlayout)
    {
        this.PillName = pillname;
        this.PillType = pilltype;
        this.PillCount = pillcount;
        this.MainLayout = mainlayout;
    }

    public void SetPillCount(int count)
    {
        this.PillCount = count;
    }


    public String GetPillName()
    {
        return this.PillName;
    }
    public LinearLayout GetMainLayout()
    {
        return this.MainLayout;
    }

    public int GetPillCount()
    {
        return this.PillCount;
    }

}
