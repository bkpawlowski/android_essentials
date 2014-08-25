package pl.pawlowski.bartek.supplib.alarm_management.management;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pl.pawlowski.bartek.supplib.alarm_management.utilities.OnAlarmStateChangeListener;

/**
 *
 */
public final class ExtendedAlarmManager implements OnAlarmStateChangeListener {

    public static final String ALARM_IDENTIFIER = "id";

    private static ExtendedAlarmManager thisClassInstance;
    private AlarmManager am;
    private Context ctx;

    private ExtendedAlarmManager(Context ctx){
        if(ctx == null){
            throw new NullPointerException("context cannot be null");
        }
        this.ctx = ctx;
        this. am = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
    }

    public static ExtendedAlarmManager getInstance(Context ctx){
        if(thisClassInstance == null){
            thisClassInstance = new ExtendedAlarmManager(ctx);
        }

        return thisClassInstance;
    }

    /**
     * Planuje alarm
     * @param alarmIdentifier - identyfikator alarmu
     * @param timeInFuture - czas w przyszlosci w ktorym zostanie uaktywniony alarm
     * @param alarmReceiverClass - klasa obslugujaca zdarzenie o uaktywnieniu alarmu
     */
    public void scheduleAlarm(int alarmIdentifier, long timeInFuture, Class<? extends BroadcastReceiver> alarmReceiverClass){
        Intent intent = new Intent(ctx, alarmReceiverClass);
        intent.putExtra(ALARM_IDENTIFIER, alarmIdentifier);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarmIdentifier, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInFuture, pendingIntent);
    }

    /**
     * Odwoluje zaplanowany alarm
     * @param alarmIdentifier - identyfikator alarmu
     * @param alarmReceiverClass - klasa obslugujaca zdarzenie o uaktywnieniu alarmu
     */
    public void cancelAlarm(int alarmIdentifier, Class<? extends BroadcastReceiver> alarmReceiverClass){
        Intent intent = new Intent(ctx, alarmReceiverClass);
        intent.putExtra(ALARM_IDENTIFIER, alarmIdentifier);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarmIdentifier, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);
    }

    @Override
    public void onAlarmScheduled(int alarmId, long timeInFuture) {
        
    }

    @Override
    public void onAlarmCancelled(int alarmId) {

    }

    @Override
    public void onAlarmFired(int alarmId) {

    }
}
