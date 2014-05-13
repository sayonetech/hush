package com.sayone.hush;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.*;

public class EventListAdapter extends ArrayAdapter<CalendarEvent> {

	  private final Activity context;
	  private final ArrayList<CalendarEvent> events;
	  SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
	  static class ViewHolder {
	    public TextView timeStart;
	    public TextView timeEnd;
		public TextView event;
	  }

	  public EventListAdapter(Activity context, ArrayList<CalendarEvent> events) {
	    super(context, R.layout.event_item, events);
	    this.context = context;
	    this.events = events;

	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.event_item, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.event = (TextView) rowView.findViewById(R.id.event);
	      viewHolder.timeStart = (TextView) rowView.findViewById(R.id.timeStart);
	      viewHolder.timeEnd = (TextView) rowView.findViewById(R.id.timeEnd);
	      rowView.setTag(viewHolder);




	    }



	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String s = events.get(position).getTitle();
	    holder.event.setText(s);
	    String t1 = df.format(events.get(position).getBegin());
	    holder.timeStart.setText(t1);
	    String t2 = df.format(events.get(position).getEnd());
	    holder.timeEnd.setText(t2);

	    return rowView;
	  }


}