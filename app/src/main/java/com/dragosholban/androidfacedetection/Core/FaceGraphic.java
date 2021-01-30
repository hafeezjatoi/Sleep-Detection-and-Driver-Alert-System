package com.dragosholban.androidfacedetection.Core;

import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dragosholban.androidfacedetection.Activities.VideoFaceDetectionActivity;
import com.dragosholban.androidfacedetection.Models.ConfigModel;
import com.google.android.gms.vision.face.Face;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.dragosholban.androidfacedetection.Activities.VideoFaceDetectionActivity.context;

public class FaceGraphic extends GraphicOverlay.Graphic
{
    private int mFaceId;
    private volatile Face mFace;
    private int milliseconds = 0;
    private Handler handler;
    private boolean IsStarted = false;
    private AlertsHandler _alerter;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);
        _alerter = new AlertsHandler(context);
        VideoFaceDetectionActivity.mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor as = context.getAssets().openFd("alarm.wav");
            VideoFaceDetectionActivity.mediaPlayer.setDataSource(as.getFileDescriptor(), as.getStartOffset(), as.getLength());
            as.close();

            VideoFaceDetectionActivity.mediaPlayer.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        handler = new Handler(Looper.getMainLooper());
    }

    public void setId(int id)
    {
        mFaceId = id;
    }

    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    private void Display(Canvas canvas, String text, float tsize, int x, int y)
    {
        try {
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setTextSize(tsize);
            canvas.drawText(text, x, y, p);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        Evaluate(face, canvas);
    }

    public Runnable runnable = new Runnable()
    {
        public void run()
        {
            milliseconds++;

            if(milliseconds >= 1500)
                milliseconds = 0;

            handler.postDelayed(this, 1);
        }
    };

    private void StartTimer()
    {
        IsStarted = true;
        handler.postDelayed(runnable, 1);
    }

    private void StopTimer()
    {
        handler.removeCallbacks(runnable);
        IsStarted = false;
        milliseconds = 0;
    }

    private void PlayAlarm()
    {
        try
        {
            if(!VideoFaceDetectionActivity.mediaPlayer.isPlaying())
                VideoFaceDetectionActivity.mediaPlayer.start();

            if(_alerter != null)
                _alerter.AlertOthers();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private double Parse(float num)
    {
        BigDecimal dec = new BigDecimal(Float.toString(num));
        dec = dec.setScale(2, RoundingMode.HALF_UP);
        return dec.floatValue();
    }

    private void Evaluate(Face face, Canvas canvas)
    {
        boolean flag = false;

        try
        {
            float x = translateX(face.getPosition().x + face.getWidth() / 2);
            float y = translateY(face.getPosition().y + face.getHeight() / 2);
            float xOffset = scaleX(face.getWidth() / 2.0f);
            float yOffset = scaleY(face.getHeight() / 2.0f);
            float left = x - xOffset;
            float top = y - yOffset;
            float right = x + xOffset;
            float bottom = y + yOffset;

            Display(canvas, "R: " + Parse(face.getIsRightEyeOpenProbability()), 50, 10, 50);
            Display(canvas, "L: " + Parse(face.getIsLeftEyeOpenProbability()), 50, 10, 150);

            if(ConfigModel.CONSIDER_RIGHT_EYE && ConfigModel.CONSIDER_LEFT_EYE)
               flag = (Parse(face.getIsLeftEyeOpenProbability()) > (ConfigModel.LEYE_THRESHOLD/100)) && (Parse(face.getIsRightEyeOpenProbability()) > (ConfigModel.REYE_THRESHOLD/100));
            else if(ConfigModel.CONSIDER_RIGHT_EYE)
                flag = face.getIsRightEyeOpenProbability() > (ConfigModel.REYE_THRESHOLD/100);
            else if(ConfigModel.CONSIDER_LEFT_EYE)
                flag = face.getIsLeftEyeOpenProbability() > (ConfigModel.LEYE_THRESHOLD/100);

            flag = (face.getIsLeftEyeOpenProbability() > 0.5) && (face.getIsRightEyeOpenProbability() > 0.5);
            flag = !flag;
            Paint p = new Paint();
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(10);

            if(flag)
            {
                if(!IsStarted)
                   StartTimer();
                else
                {
                    if(milliseconds > ConfigModel.DROWSYNESS_DURATION)
                    {
                        Log.i("Sleep Detector", "Sleep Detected Both Eyes Closed!");
                        p.setColor(Color.RED);
                        Display(canvas, "Alert!", 150, canvas.getWidth()/4, canvas.getHeight()/4);
                        PlayAlarm();
                    }
                }
            }
            else {
                p.setColor(Color.GREEN);

                if(IsStarted)
                  StopTimer();
            }

            canvas.drawRect(left, top, right, bottom, p);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}