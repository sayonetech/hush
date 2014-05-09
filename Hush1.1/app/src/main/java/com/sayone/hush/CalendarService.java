package com.sayone.hush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;

public class CalendarService {

	// Default constructor
	public static HashMap<String, List<CalendarEvent>> readCalendar(Context context) {
		return readCalendar(context, 1, 0);
	}

	// Use to specify specific the time span
	public static HashMap<String, List<CalendarEvent>> readCalendar(Context context, int days, int hours) {

		ContentResolver contentResolver = context.getContentResolver();

		// Create a cursor and read from the calendar (for Android API below
		// 4.0)
		final Cursor cursor = contentResolver
				.query(Uri.parse("content://com.android.calendar/calendars"),
						(new String[] {
								CalendarContract.Calendars._ID,
								CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
								CalendarContract.Calendars.VISIBLE }), null,
						null, null);

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
					new String[] { Events.TITLE, Events.DTSTART, Events.DTEND, Events._ID },
					Events.CALENDAR_ID +"="+ id, null,
					"startDay ASC, startMinute ASC");

			System.out.println("eventCursor count=" + eventCursor.getCount());

			// If there are actual events in the current calendar, the count
			// will exceed zero
			if (eventCursor.getCount() > 0) {

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

				System.out.println(ce.toString());

				// While there are more events in the current calendar, move to
				// the next instance
				while (eventCursor.moveToNext()) {

					// Adds the object to the list of events
					ce = loadEvent(eventCursor);
					eventList.add(ce);

					System.out.println(ce.toString());

				}

				Collections.sort(eventList);
				eventMap.put(id, eventList);

				System.out.println(eventMap.keySet().size() + " "
						+ eventMap.values());
				return eventMap;
			}
		}
		return null;
	}

	// Returns a new instance of the calendar object
	private static CalendarEvent loadEvent(Cursor csr) {
		return new CalendarEvent(csr.getString(0), new Date(csr.getLong(1)),
				new Date(csr.getLong(2)), csr.getString(3));
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
					String displayName = cursor.getString(1);
					Boolean selected = !cursor.getString(2).equals("0");

					System.out.println("Id: " + _id + " Display Name: "
							+ displayName + " Selected: " + selected);
					calendarIds.add(_id);

				}
			}
		}

		catch (AssertionError ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return calendarIds;

	}
}
