package pl.pawlowski.bartek.supplib.alarm_management.utilities;

/**
 * TODO javadoc
 */
public interface OnAlarmStateChangeListener {
    /**
     * Called back when alarm has just been scheduled
     * @param alarmId - alarm identifier
     * @param timeInFuture - time in future when alarm should be fired
     */
    public void onAlarmScheduled(int alarmId, long timeInFuture);

    /**
     * Called back when alarm has just been cancelled
     * @param alarmId - alarm identifier
     */
    public void onAlarmCancelled(int alarmId);

    /**
     * Called back when alarm has just been fired
     * @param alarmId - alarm identifier
     */
    public void onAlarmFired(int alarmId);
}
