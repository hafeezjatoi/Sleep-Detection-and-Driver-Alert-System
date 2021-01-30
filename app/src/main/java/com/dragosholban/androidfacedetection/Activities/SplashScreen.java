package com.dragosholban.androidfacedetection.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.dragosholban.androidfacedetection.Activities.MainActivity;
import com.dragosholban.androidfacedetection.R;

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        WaitForSomeTime();
    }

    private void WaitForSomeTime()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent _intent = new Intent(getBaseContext(), Login.class);
                startActivity(_intent);
                finish();
            }
        }, 4500);
    }
}
