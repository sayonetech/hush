package com.sayone.hush;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CalendarEventChangeReceiver extends BroadcastReceiver {
	@Override


	public void onReceive(Context context, Intent intent) {
		android.util.Log.i("Calendar Reciever",
				"CalendarTest.onReceive called!");
		HashMap<String, List<CalendarEvent>> events = CalendarService
				.readCalendar(context);
		if(events == null) return;
		for (String key : events.keySet()) {
			for (CalendarEvent event : events.get(key)) {
				System.out.println("title :" + event.getTitle() + " : id"
						+ event.getId());
				long now = new Date().getTime();
                String st=event.getTitle();
                if(st.equals("cls") || st.equals("Meeting")||st.equals("meeting")||st.equals("class")||st.equals("Class")||st.equals("Cls"))
                {

                    if (event.getEnd().getTime() > now) {
                        System.out.println("setting alarm for:" + event.getTitle());
                        AlarmHandler.SetAlarm(context, event.getBegin().getTime(),
                                Integer.parseInt(event.getId()) * 10
                                        + AlarmHandler.EVENT_START
                        );
                        AlarmHandler.SetAlarm(context, event.getEnd().getTime(),
                                Integer.parseInt(event.getId()) * 10
                                        + AlarmHandler.EVENT_END
                        );
                    }
                }

			}
		}

	}
}
