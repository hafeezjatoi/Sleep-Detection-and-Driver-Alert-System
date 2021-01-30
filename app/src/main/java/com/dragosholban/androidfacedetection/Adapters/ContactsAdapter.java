package com.dragosholban.androidfacedetection.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragosholban.androidfacedetection.Core.DBHelper;
import com.dragosholban.androidfacedetection.Models.DataModel;
import com.dragosholban.androidfacedetection.R;
import com.dragosholban.androidfacedetection.Activities.Types_of_Alert;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    private static class ViewHolder
    {
        TextView txtPhone;
        ImageView bell;
        ImageView remove;
    }

    public ContactsAdapter(ArrayList<DataModel> data, Context context)
    {
        super(context, R.layout.phone_no, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v)
    {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {
            case R.id.remove_contact:
                try
                {
                    DBHelper _helper = new DBHelper(mContext);
                    _helper.DeleteContact(dataModel.GetID());
                    dataSet.remove(dataModel);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Success!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.alerts_type:
                Intent intent = new Intent(mContext, Types_of_Alert.class);
                mContext.startActivity(intent);
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.phone_no, parent, false);

            viewHolder.txtPhone = (TextView) convertView.findViewById(R.id.phoneno);
            viewHolder.bell = (ImageView) convertView.findViewById(R.id.alerts_type);
            viewHolder.remove = (ImageView) convertView.findViewById(R.id.remove_contact);

            result=convertView;

            convertView.setTag(viewHolder);
        }
        else
            {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtPhone.setText(dataModel.GetNumber());

        viewHolder.remove.setOnClickListener(this);
        viewHolder.bell.setOnClickListener(this);

        viewHolder.bell.setTag(position);
        viewHolder.remove.setTag(position);

        return convertView;
    }
}
