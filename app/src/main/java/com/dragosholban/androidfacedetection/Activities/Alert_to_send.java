package com.dragosholban.androidfacedetection.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dragosholban.androidfacedetection.Adapters.ContactsAdapter;
import com.dragosholban.androidfacedetection.Core.DBHelper;
import com.dragosholban.androidfacedetection.Models.DataModel;
import com.dragosholban.androidfacedetection.R;

import java.util.ArrayList;


public class Alert_to_send extends AppCompatActivity
{
    ListView listview;
    Button Addbutton;
    EditText GetValue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_to_send);

        listview = (ListView) findViewById(R.id.listView_phone);
        Addbutton = (Button) findViewById(R.id.btn_save_phone);
        GetValue = (EditText) findViewById(R.id.editText1);


        Addbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String number = GetValue.getText().toString();
                if(number != null && number != "")
                {
                    if(number.length() == 11)
                    {
                        number = number.replaceFirst("0", "+92");

                        DBHelper _helper = new DBHelper(getBaseContext());
                        if(_helper.InsertPhone(number))
                            Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getBaseContext(), "Oops! Error Occured!", Toast.LENGTH_SHORT).show();

                        GetValue.setText("");
                        PopulateList();
                    }
                    else
                        Toast.makeText(getBaseContext(), "Enter phone number with proper format, Including country code", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getBaseContext(), "The number field cannot be left blank!", Toast.LENGTH_SHORT).show();
            }
        });

        PopulateList();
    }

    private void PopulateList()
    {
        try
        {
            DBHelper _helper = new DBHelper(getBaseContext());
            ArrayList<DataModel> contacts = _helper.GetAllCotacts();
            ContactsAdapter _adapter = new ContactsAdapter(contacts, getBaseContext());
            listview.setAdapter(_adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
