package com.tqri.activity;

import android.app.AlarmManager; 
import android.app.PendingIntent; 
import android.content.Context; 
import android.content.Intent;

public class AlarmHelper { 
  
    private Context context; 
    private AlarmManager mAlarmManager; 
  
    public AlarmHelper(Context context) { 
        this.context = context; 
        mAlarmManager = (AlarmManager) context 
                .getSystemService(Context.ALARM_SERVICE); 
    } 
  
    public void openAlarm(int id,String content,long time) { 
        Intent intent = new Intent(); 
        intent.putExtra("content", content);
        intent.putExtra("time", time);
        intent.setClass(context,CallAlarm.class); 
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 
                PendingIntent.FLAG_UPDATE_CURRENT); 
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent); 
    } 
  
    public void closeAlarm(int id, String title, String content) { 
        Intent intent = new Intent(); 
        intent.putExtra("_id", id); 
        intent.putExtra("title", title); 
        intent.putExtra("content", content); 
        intent.setClass(context, CallAlarm.class); 
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0); 
        mAlarmManager.cancel(pendingIntent); 
    } 
}
