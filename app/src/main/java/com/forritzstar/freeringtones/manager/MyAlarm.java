package com.forritzstar.freeringtones.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.forritzstar.freeringtones.MyApp;

/**
 * Created by zzz on 14-11-20.
 */
public class MyAlarm {
    private static long interval;

    public static void setAlarm() {
        interval = MyPreference.getAlarmInterval();
        set(AlarmReceiver.class, MyPreference.PREF_ALARM_TRIGGER);
    }

    public static void setRingtone() {
        interval = MyPreference.getRingtoneInterval();
        set(RingtoneReceiver.class, MyPreference.PREF_RINGTONE_TRIGGER);
    }

    public static void setNotification() {
        interval = MyPreference.getNotificationInterval();
        set(NotificationReceiver.class, MyPreference.PREF_NOTIFICATION_TRIGGER);
    }

    private static void set(Class<?> cls, String triggerKey) {
        AlarmManager alarmMgr = (AlarmManager) MyApp.app.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setInexactRepeating(AlarmManager.RTC, getTriggerAtMillis(triggerKey),
                interval, getPendIntent(cls));
    }

    private static long getTriggerAtMillis(String triggerKey) {
        long max = Math.max(MyPreference.getLong(triggerKey), System.currentTimeMillis());
        return Math.min(max, System.currentTimeMillis() + interval);
    }

    public static void cancel(Class<?> cls) {
        AlarmManager alarmMgr = (AlarmManager) MyApp.app.getSystemService(Context.ALARM_SERVICE);
        // 与上面的intent匹配（filterEquals(intent)）的闹钟会被取消
        alarmMgr.cancel(getPendIntent(cls));
    }

    private static PendingIntent getPendIntent(Class<?> cls) {
        Context context = MyApp.app;
        Intent intent = new Intent(context, cls);
        int requestCode = 0;
        return PendingIntent.getBroadcast(context,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
