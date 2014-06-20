package com.sayone.hush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.Toast;

public class CalendarService {
    private static int passed;


    // Default constructor
    public static HashMap<String, List<CalendarEvent>> readCalendar(Context context) {
        return readCalendar(context, 1, 0);
    }

    // Use to specify specific the time span
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static HashMap<String, List<CalendarEvent>> readCalendar(Context context, int days, int hours) {

        //SharedPreferences for getting events count
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);  //me
        SharedPreferences.Editor editor = pref.edit();


        MainActivity playObject = new MainActivity();  //me
        passed = playObject.getCategory();            //me

        ContentResolver contentResolver = context.getContentResolver();

        // Create a cursor and read from the calendar (for Android API below
        // 4.0)
        final Cursor cursor = contentResolver
                .query(Uri.parse("content://com.android.calendar/calendars"),
                        (new String[]{
                                CalendarContract.Calendars._ID,
                                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                                CalendarContract.Calendars.VISIBLE}), null,
                        null, null, null
                );



		/*
         * Use the cursor below for Android API 4.0+
		 * 
		 * Cursor cursor =
		 * contentResolver.query(Uri.parse("content://com.android.calendar/events"
		 * ), new String[]{ "calendar_id", "title", "description", "dtstart",
		 * "dtend", "eventLocation" }, null, null, null);
		 */

        // Create a set containing all of the calendar IDs available on the
        // phone
        HashSet<String> calendarIds = getCalenderIds(cursor);

        // Create a hash map of calendar ids and the events of each id
        HashMap<String, List<CalendarEvent>> eventMap = new HashMap<String, List<CalendarEvent>>();

        // Loop over all of the calendars
        for (String id : calendarIds) {

            // Create a builder to define the time span
            Uri.Builder builder = Uri.parse(
                    "content://com.android.calendar/instances/when")
                    .buildUpon();
            long now = new Date().getTime();

            // create the time span based on the inputs
            ContentUris.appendId(builder, now
                    - (DateUtils.DAY_IN_MILLIS * days)
                    - (DateUtils.HOUR_IN_MILLIS * hours));
            ContentUris.appendId(builder, now
                    + (DateUtils.DAY_IN_MILLIS * days)
                    + (DateUtils.HOUR_IN_MILLIS * hours));

            // Create an event cursor to find all events in the calendar

            Cursor eventCursor = contentResolver.query(builder.build(),
                    new String[]{Events.TITLE, Events.DTSTART, Events.DTEND, Events._ID, Events.STATUS},
                    Events.CALENDAR_ID + "=" + id, null,
                    "startDay ASC, startMinute ASC");


            System.out.println("eventCursor count=" + eventCursor.getCount());

            //SharedPreferences for getting events count                                              //me
            editor.putInt("Event_Count", eventCursor.getCount());
            editor.commit();


            // If there are actual events in the current calendar, the count
            // will exceed zero

            if (eventCursor.getCount() > 0) {

                passed = playObject.getCategory();            //me
                if (passed == 0) {  ///me


                    // Create a list of calendar events for the specific calendar
                    List<CalendarEvent> eventList = new ArrayList<CalendarEvent>();

                    // Move to the first object
                    eventCursor.moveToFirst();

                    // Create an object of CalendarEvent which contains the title,
                    // when the event begins and ends,
                    // and if it is a full day event or not
                    CalendarEvent ce = loadEvent(eventCursor);

                    passed = playObject.getCategory();


                    if (passed == 0 && ce.getTitle().equals("cls") || ce.getTitle().equals("Meeting") || ce.getTitle().equals("meeting") || ce.getTitle().equals("class") || ce.getTitle().equals("Class") || ce.getTitle().equals("Cls")) {  ///me

                        // Adds the first object to the list of events
                        eventList.add(ce);
                        // Toast.makeText(context,"1.passed==0=:"+passed,Toast.LENGTH_LONG).show();      //me


                        System.out.println(ce.toString());


                    }   ////me
                    passed = playObject.getCategory();//me


                    // While there are more events in the current calendar, move to
                    // the next instance

                    while (eventCursor.moveToNext()) {

                        passed = playObject.getCategory();//me


                        if (passed == 0 && loadEvent(eventCursor).getTitle().equals("cls") || loadEvent(eventCursor).getTitle().equals("Meeting") || loadEvent(eventCursor).getTitle().equals("meeting") || loadEvent(eventCursor).getTitle().equals("class") || loadEvent(eventCursor).getTitle().equals("Class") || loadEvent(eventCursor).getTitle().equals("Cls")) {  ///me

                            // Adds the object to the list of events
                            ce = loadEvent(eventCursor);
                            eventList.add(ce);

                            System.out.println(ce.toString());


                        }    //me


                    }
                    passed = playObject.getCategory();//me

                    Collections.sort(eventList);
                    eventMap.put(id, eventList);

                    System.out.println(eventMap.keySet().size() + " "
                            + eventMap.values());
                    return eventMap;

                } ///me

                else  //me
                {
                    //  passed = playObject.getCategory();            //me


                    // Create a list of calendar events for the specific calendar
                    List<CalendarEvent> eventList = new ArrayList<CalendarEvent>();

                    // Move to the first object
                    eventCursor.moveToFirst();

                    // Create an object of CalendarEvent which contains the title,
                    // when the event begins and ends,
                    // and if it is a full day event or not
                    CalendarEvent ce = loadEvent(eventCursor);
                    // Adds the first object to the list of events
                    eventList.add(ce);
                    // Toast.makeText(context,"1.passed==0=:"+passed,Toast.LENGTH_LONG).show();      //me


                    System.out.println(ce.toString());
                    passed = playObject.getCategory();//me


                    // While there are more events in the current calendar, move to
                    // the next instance

                    while (eventCursor.moveToNext()) {


                        // Adds the object to the list of events
                        ce = loadEvent(eventCursor);
                        eventList.add(ce);

                        System.out.println(ce.toString());
                        passed = playObject.getCategory();//me


                    }
                    passed = playObject.getCategory();//me

                    Collections.sort(eventList);
                    eventMap.put(id, eventList);

                    System.out.println(eventMap.keySet().size() + " "
                            + eventMap.values());
                    return eventMap;


                }


            }
        }

        return null;
    }

    // Returns a new instance of the calendar object
    private static CalendarEvent loadEvent(Cursor csr) {
        return new CalendarEvent(csr.getString(0), new Date(csr.getLong(1)),
                new Date(csr.getLong(2)), csr.getString(3), csr.getString(4));
    }

    // Creates the list of calendar ids and returns it in a set
    private static HashSet<String> getCalenderIds(Cursor cursor) {

        HashSet<String> calendarIds = new HashSet<String>();

        try {

            // If there are more than 0 calendars, continue
            if (cursor.getCount() > 0) {

                // Loop to set the id for all of the calendars
                while (cursor.moveToNext()) {

                    String _id = cursor.getString(0);
                    //  String STATUS="true";

                    String displayName = cursor.getString(1);
                    Boolean selected = !cursor.getString(2).equals("0");

                    System.out.println("Id: " + _id + " Display Name: "
                            + displayName + " Selected: " + selected);
                    calendarIds.add(_id);


                }
            }
        } catch (AssertionError ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return calendarIds;

    }
}
