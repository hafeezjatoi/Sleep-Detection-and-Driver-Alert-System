package com.dragosholban.androidfacedetection.Core;

import android.content.Context;
import android.telephony.SmsManager;

import com.dragosholban.androidfacedetection.Models.ConfigModel;
import com.dragosholban.androidfacedetection.Models.DataModel;

import java.util.ArrayList;

public class AlertsHandler
{
    private int count;
    private Context context;

    private void SendSMS(String msg, String num)
    {
        try
        {
            if(num.startsWith("0")) {
                num = "+92" + num.substring(1);
            }
            else if(num.contains("-"))
            {
                num = num.replace("-", "");
            }

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, msg, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public AlertsHandler(Context context)
    {
        count = 0;
        this.context = context;
    }

    public void AlertOthers()
    {
        count++;
        if(count >= ConfigModel.NUM_OF_ALERTS)
        {
            DBHelper _db = new DBHelper(context);
            String msg = ConfigModel.ALERT_TO_SEND.replace("%reg%", ConfigModel.VEHICLE_REG_NO);
            ArrayList<DataModel> contacts = _db.GetAllCotacts();
            for(DataModel contact : contacts)
            {
                SendSMS(msg, contact.GetNumber());
            }
        }
    }
}
