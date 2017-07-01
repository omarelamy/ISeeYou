package com.example.omarelaimy.iseeyou;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hp on 01-Jul-17.
 */

public class Pill {
    private String PillName;
    private int PillType;
    private int PillCount;
    private int Position;
    private ImageView Pillimg;
    private ImageView Closeimg;
    private TextView Pilltv;

    public Pill()
    {
        //Empty constructor.
    }
    //Setting the attributes for a patient.
   public void SetPillInfo(String pillname,int pilltype, int pillcount, int pos, ImageView pillimg,ImageView closeimg, TextView pilltv)
   {
       this.PillName = pillname;
       this.PillType = pilltype;
       this.PillCount = pillcount;
       this.Position = pos;
       this.Pillimg = pillimg;
       this.Closeimg = closeimg;
       this.Pilltv = pilltv;
   }

   public String GetPillName()
   {
       return PillName;
   }

   public int GetPillType()
    {
        return PillType;
    }

    public int GetPillCount()
    {
        return PillCount;
    }

    public int GetPosition()
    {
        return Position;
    }

    public ImageView GetPillimg()
    {
        return Pillimg;
    }

    public ImageView GetCloseimg()
    {
        return Closeimg;
    }

    public TextView GetPilltv()
    {
        return Pilltv;
    }







}
