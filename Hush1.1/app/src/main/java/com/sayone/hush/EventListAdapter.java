package com.sayone.hush;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.widget.Toast.*;

public class EventListAdapter extends ArrayAdapter<CalendarEvent> {

    boolean checkAll_flag = false;  //me
    boolean checkItem_flag = false;//me
    int event_count;  //me
    public String st;


    private final Activity context;
    private ArrayList<CalendarEvent> events;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
    public String tag;

    static class ViewHolder {
        public TextView timeStart;
        public TextView timeEnd;
        public TextView event;
        // public TextView sta;
        public int Event_count;
        public CheckBox checkBox;

    }

    public EventListAdapter(Activity context, ArrayList<CalendarEvent> events) {
        super(context, R.layout.event_item, events);
        this.context = context;
        this.events = events;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //final ViewHolder viewHolder1=null;

        //SharedPreferences                                                                           //me
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = pref.edit();
        event_count = pref.getInt("Event_Count", 0);


        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.event_item, null);
            viewHolder = new ViewHolder();//me
            viewHolder.event = (TextView) rowView.findViewById(R.id.event);
            viewHolder.timeStart = (TextView) rowView.findViewById(R.id.timeStart);
            viewHolder.timeEnd = (TextView) rowView.findViewById(R.id.timeEnd);
            // viewHolder.sta = (TextView) rowView.findViewById(R.id.sta);

            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox1);

            rowView.setTag(viewHolder);

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //me

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    events.get(getPosition).setSelected(buttonView.isChecked());

                    if (finalViewHolder.checkBox.isChecked())// Set the value of checkbox to maintain its state.
                    {

                        events.get(getPosition).setStatus("true");
                        // finalViewHolder.sta.setText(events.get(getPosition).getStatus());


                        editor.putString("ID" + events.get(getPosition).getId(), events.get(getPosition).getId());//me

                        editor.commit();

                    } else {

                        String tag1 = "false";
                        events.get(getPosition).setStatus("false");
                        // finalViewHolder.sta.setText(events.get(getPosition).getId());
                        editor.putString("ID" + events.get(getPosition).getId(), "false");//me
                        editor.commit();
                    }
                    st = events.get(getPosition).getStatus();


                }
            });


        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d("position" ,""+position);
        viewHolder.checkBox.setTag(position);//me

        ViewHolder holder = (ViewHolder) rowView.getTag();
        String s = events.get(position).getTitle();
        holder.event.setText(s);
        String t1 = df.format(events.get(position).getBegin());
        holder.timeStart.setText(t1);
        String t2 = df.format(events.get(position).getEnd());
        holder.timeEnd.setText(t2);

        if (pref.getString("ID" + events.get(position).getId(), "0").equals(events.get(position).getId())) {

            viewHolder.checkBox.setChecked(true);

        } else {

            viewHolder.checkBox.setChecked(false);

        }
        if (!events.get(position).getId().equals(pref.getString("ID" + events.get(position).getId(), "0")) && !pref.getString("ID" + events.get(position).getId(), "0").equals("false")) {
            editor.putString("ID" + events.get(position).getId(), events.get(position).getId());//me
            editor.commit();
            viewHolder.checkBox.setChecked(true);

        }
        if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.parseColor("#FFFAC6"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        return rowView;

    }

}