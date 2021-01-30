package com.dragosholban.androidfacedetection.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dragosholban.androidfacedetection.Core.ConfigHandler;
import com.dragosholban.androidfacedetection.Models.ConfigModel;
import com.dragosholban.androidfacedetection.R;
import com.dragosholban.androidfacedetection.about_us;
import com.dragosholban.androidfacedetection.more_us;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onConfigureClick(View view)
    {
        Intent _intent = new Intent(this, Configurations.class);
        startActivity(_intent);
    }

    public void onStartMonitoringClick(View view)
    {
        Intent intent = new Intent(this, VideoFaceDetectionActivity.class);
        startActivity(intent);
    }

    public void onAboutUsClick(View view)
    {
        Intent intent = new Intent(this, about_us.class);
        startActivity(intent);
    }

    public void onMoreUsClick(View view)
    {
        Intent intent = new Intent(this, more_us.class);
        startActivity(intent);
    }


    private void Init()
    {
        try
        {
            boolean flag = Boolean.parseBoolean(ConfigHandler.Load("ConsiderRightEye", "true", getBaseContext()));
            ConfigModel.CONSIDER_RIGHT_EYE = flag;

            flag = Boolean.parseBoolean(ConfigHandler.Load("ConsiderLeftEye", "true", getBaseContext()));
            ConfigModel.CONSIDER_LEFT_EYE = flag;

            int value = Integer.parseInt(ConfigHandler.Load("sb_Reye", "50", this));
            ConfigModel.REYE_THRESHOLD = value;

            value = Integer.parseInt(ConfigHandler.Load("sb_Leye", "50", this));
            ConfigModel.LEYE_THRESHOLD = value;

            value = Integer.parseInt(ConfigHandler.Load("sb_duration", "700", this));
            ConfigModel.DROWSYNESS_DURATION = value;

            ConfigModel.NUM_OF_ALERTS = Integer.parseInt(ConfigHandler.Load("txt_num_of_alerts", "3", this));
            ConfigModel.ALERT_TO_SEND = ConfigHandler.Load("txt_msg", "Dear User! One of your vehicle %reg% is under threat, Driver is feeling sleepy & fatigue! Kindly take serious action", this);
            ConfigModel.VEHICLE_REG_NO = ConfigHandler.Load("txt_reg", "ZMD-3453", this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Init();
    }

}

