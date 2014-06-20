package com.sayone.hush;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class AlarmHandler extends BroadcastReceiver 
{    
	public final static int EVENT_START = 0;
	public final static int EVENT_END = 1;
    public String status,id;
    public  int event_count;
     @Override
     public void onReceive(Context context, Intent intent) 
     {   
         PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
         int code = intent.getExtras().getInt("requestCode");
         int type = code%10;
         int _id=code/10;
         AudioManager am;
         am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

          SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
          SharedPreferences.Editor editor = pref.edit();
         event_count=pref.getInt("Event_Count",0);



         if (!pref.getString("ID"+_id,"0").equals("false"))
         {

            // Toast.makeText(context, "state :_ "+pref.getString("ID"+_id,"0"), Toast.LENGTH_LONG).show();

         //For Normal mode
         if(type == EVENT_END){
        	 Toast.makeText(context, "Back to normal ", Toast.LENGTH_LONG).show(); // For example
        	 am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
         }
         
         else {
        	 Toast.makeText(context, "Changing to silent " , Toast.LENGTH_LONG).show();
        	 am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
             am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
         }
         
         System.out.println("Changing to silent mode");
         }
     }

    private Intent getIntent() {
        return null;
    }

    public static void SetAlarm(Context context, long time, int code)
 {
     AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
     Intent i = new Intent(context, AlarmHandler.class);
     i.putExtra("requestCode", code);
     PendingIntent pi = PendingIntent.getBroadcast(context, code, i, 0);
     
     am.set(AlarmManager.RTC_WAKEUP, time, pi);
 }

 public void CancelAlarm(Context context)
 {
     Intent intent = new Intent(context, AlarmHandler.class);
     PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
     AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
     alarmManager.cancel(sender);
 }
}