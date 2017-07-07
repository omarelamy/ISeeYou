package com.example.omarelaimy.iseeyou;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by hp on 01-Jul-17.
 */

public class Pill {
    private String PillName;
    private int PillType;
    private int PillCount;
    private int Position;
    private LinearLayout MainLayout;

    public Pill()
    {
        //Empty constructor.
    }
    //Setting the attributes for a patient.
   public void SetPillInfo(String pillname,int pilltype, int pillcount, int pos, LinearLayout mainlayout)
   {
       this.PillName = pillname;
       this.PillType = pilltype;
       this.PillCount = pillcount;
       this.Position = pos;
       this.MainLayout = mainlayout;
   }

   public void SetCount(int count)
   {
       this.PillCount = count;
   }

   public String GetPillName()
   {
       return this.PillName;
   }

   public int GetPillType()
    {
        return this.PillType;
    }

    public int GetPillCount()
    {
        return this.PillCount;
    }

    public int GetPosition()
    {
        return this.Position;
    }

    public LinearLayout GetPillLayout()
    {
        return this.MainLayout;
    }

}
