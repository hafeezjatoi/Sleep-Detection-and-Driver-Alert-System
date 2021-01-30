package com.dragosholban.androidfacedetection.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dragosholban.androidfacedetection.Core.ConfigHandler;
import com.dragosholban.androidfacedetection.Models.ConfigModel;
import com.dragosholban.androidfacedetection.R;

public class Configurations extends AppCompatActivity
{
    private Button btn_contacts, btn_save;

    private SeekBar sb_rEye, sb_lEye, sb_Drowsiness;
    private TextView lb_Reye, lb_Leye, lb_duration;
    private CheckBox cb_reye, cb_leye;
    private EditText txt_msg, txt_reg, txt_numofAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);

        btn_contacts = (Button)findViewById(R.id.btn_configure_numbers);
        btn_save = (Button)findViewById(R.id.btn_save_settings);

        sb_rEye = (SeekBar)findViewById(R.id.sb_Reye);
        sb_lEye = (SeekBar)findViewById(R.id.sb_Leye);
        sb_Drowsiness = (SeekBar)findViewById(R.id.sb_Drowsiness_Duration);

        lb_Reye = (TextView)findViewById(R.id.lb_rValue);
        lb_Leye = (TextView)findViewById(R.id.lb_lValue);
        lb_duration = (TextView)findViewById(R.id.lb_drowsyvalue);

        cb_reye = (CheckBox)findViewById(R.id.cb_rightEye);
        cb_leye = (CheckBox)findViewById(R.id.cb_LeftEye);

        txt_msg = (EditText)findViewById(R.id.txt_Message);
        txt_reg = (EditText)findViewById(R.id.txt_reg);
        txt_numofAlerts = (EditText)findViewById(R.id.txt_Num_of_Alerts);

        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent _intent = new Intent(getBaseContext(), Alert_to_send.class);
                startActivity(_intent);
            }
        });

        cb_reye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                sb_rEye.setEnabled(isChecked);
                ConfigModel.CONSIDER_RIGHT_EYE = isChecked;
                ConfigHandler.Save("ConsiderRightEye", String.valueOf(isChecked), getBaseContext());
            }
        });

        cb_leye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                sb_lEye.setEnabled(isChecked);
                ConfigModel.CONSIDER_LEFT_EYE = isChecked;
                ConfigHandler.Save("ConsiderLeftEye", String.valueOf(isChecked), getBaseContext());
            }
        });

        sb_rEye.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                lb_Reye.setText("Closing Senstivity (" + String.valueOf(i) + "%)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if(ConfigHandler.Save("sb_Reye", String.valueOf(seekBar.getProgress()), Configurations.this))
                    Toast.makeText(Configurations.this, "Success!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Configurations.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        sb_lEye.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                lb_Leye.setText("Closing Senstivity (" + String.valueOf(i) + "%)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if(ConfigHandler.Save("sb_Leye", String.valueOf(seekBar.getProgress()), Configurations.this))
                    Toast.makeText(Configurations.this, "Success!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Configurations.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        sb_Drowsiness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                lb_duration.setText("Drowsyness Duration (" + String.valueOf(i) + "%)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if(ConfigHandler.Save("sb_duration", String.valueOf(seekBar.getProgress()), Configurations.this))
                    Toast.makeText(Configurations.this, "Success!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Configurations.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String value = txt_numofAlerts.getText().toString();
                if(value == null || value == "")
                {
                    Toast.makeText(Configurations.this, "Number of Alerts count is not given!", Toast.LENGTH_SHORT).show();
                    return;
                }

                value = txt_msg.getText().toString();
                if(value == null || value == "")
                {
                    Toast.makeText(Configurations.this, "Custom alert to send is not provided!", Toast.LENGTH_SHORT).show();
                    return;
                }

                value = txt_reg.getText().toString();
                if(value == null || value == "")
                {
                    Toast.makeText(Configurations.this, "Vehicle Registration number is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                value = txt_numofAlerts.getText().toString();
                if(Integer.parseInt(value) >= 1 && Integer.parseInt(value) <= 6)
                {
                    ConfigHandler.Save("txt_msg", txt_msg.getText().toString(), Configurations.this);
                    ConfigHandler.Save("txt_num_of_alerts", txt_numofAlerts.getText().toString(), Configurations.this);
                    ConfigHandler.Save("txt_reg", txt_reg.getText().toString(), Configurations.this);
                    Toast.makeText(Configurations.this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Init();
    }

    private void Init()
    {
        try
        {
            sb_Drowsiness.setMax(1500);

            cb_reye.setChecked(ConfigModel.CONSIDER_RIGHT_EYE);
            cb_leye.setChecked(ConfigModel.CONSIDER_LEFT_EYE);

            lb_Reye.setText("Closing Senstivity (" + String.valueOf(ConfigModel.REYE_THRESHOLD) + "%)");
            sb_rEye.setProgress(ConfigModel.REYE_THRESHOLD);

            lb_Leye.setText("Closing Senstivity (" + String.valueOf(ConfigModel.LEYE_THRESHOLD) + "%)");
            sb_lEye.setProgress(ConfigModel.LEYE_THRESHOLD);

            lb_duration.setText("Drowsyness Duration (" + String.valueOf(ConfigModel.DROWSYNESS_DURATION) + "%)");
            sb_Drowsiness.setProgress(ConfigModel.DROWSYNESS_DURATION);

            txt_numofAlerts.setText(String.valueOf(ConfigModel.NUM_OF_ALERTS));
            txt_msg.setText(ConfigModel.ALERT_TO_SEND);
            txt_reg.setText(ConfigModel.VEHICLE_REG_NO);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
