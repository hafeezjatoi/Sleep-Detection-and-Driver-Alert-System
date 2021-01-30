package com.dragosholban.androidfacedetection.Core;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ConfigHandler
{
    public static boolean Save(String key, String value, Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("_myPrivateSP", context.MODE_PRIVATE);
            SharedPreferences.Editor _editor = sp.edit();
            _editor.putString(key, value);
            Log.i("ConfigHandler", key + " : " + value);
            return _editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static String Load(String key, String def, Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("_myPrivateSP", context.MODE_PRIVATE);
            Log.i("ConfigHandler", key + " : " + sp.getString(key, def));
            return sp.getString(key, def);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return def;
        }
    }
}
