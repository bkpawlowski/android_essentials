package pl.pawlowski.bartek.supplib.alarm_management.management;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

import pl.pawlowski.bartek.supplib.alarm_management.utilities.OnAlarmStateChangeListener;

/**
 * Created by bartek-mint on 15.02.14.
 */
public class AlarmController{

    private ExtendedAlarmManager alarmManager;

    /** Listeners */
    private Set<OnAlarmStateChangeListener> onAlarmStateChangeListeners;

    private static AlarmController thisClassInstance;

    public static AlarmController getInstance(Context ctx){
        if(thisClassInstance == null){
            thisClassInstance = new AlarmController(ctx);
        }

        return thisClassInstance;
    }

    private AlarmController(Context ctx){
        onAlarmStateChangeListeners = new HashSet<OnAlarmStateChangeListener>();
        alarmManager = ExtendedAlarmManager.getInstance(ctx);
    }

    public final void scheduleAlarm(int alarmId, long timeInFuture){
        alarmManager.scheduleAlarm(alarmId, timeInFuture, AlarmBroadcastReceiver.class);
        distributeOnAlarmScheduledCallback(alarmId, timeInFuture);
    }

    public final void cancelAlarm(int alarmId){
        alarmManager.cancelAlarm(alarmId, AlarmBroadcastReceiver.class);
        distributeOnAlarmCancelledCallback(alarmId);
    }

    /**
     * Called on controller when {@link AlarmBroadcastReceiver} receives notification about fired
     * alarm from Android OS
     * @param alarmId - alarm identifier
     */
    final void onAlarmFired(int alarmId){
        distributeOnAlarmFiredCallback(alarmId);
    }

    /**
     * Calls back onAlarmScheduled method on every registered {@link OnAlarmStateChangeListener}
     * instance
     * @param alarmId - alarm identifier
     */
    protected void distributeOnAlarmCancelledCallback(int alarmId){
        for (OnAlarmStateChangeListener listener : onAlarmStateChangeListeners){
            listener.onAlarmCancelled(alarmId);
        }
    }

    /**
     * Calls back onAlarmScheduled method on every registered {@link OnAlarmStateChangeListener}
     * instance
     * @param alarmId - alarm identifier
     */
    protected void distributeOnAlarmScheduledCallback(int alarmId, long timeInFuture){
        for (OnAlarmStateChangeListener listener : onAlarmStateChangeListeners){
            listener.onAlarmScheduled(alarmId, timeInFuture);
        }
    }

    /**
     * Calls back onAlarmScheduled method on every registered {@link OnAlarmStateChangeListener}
     * instance
     * @param alarmId - alarm identifier
     */
    protected void distributeOnAlarmFiredCallback(int alarmId){
        for (OnAlarmStateChangeListener listener : onAlarmStateChangeListeners){
            listener.onAlarmFired(alarmId);
        }
    }

    /**
     *
     * registers {@link OnAlarmStateChangeListener} instance for notifications
     * @param listener
     */
    public void addOnAlarmStateChangeListener(OnAlarmStateChangeListener listener){
        onAlarmStateChangeListeners.add(listener);
    }

    /**
     * removes {@link OnAlarmStateChangeListener} instance from notifications
     * @param listener - listener instance to be removed
     */
    public void removeOnAlarmStateChangeListener(OnAlarmStateChangeListener listener){
        onAlarmStateChangeListeners.remove(listener);
    }
}
