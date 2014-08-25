package pl.pawlowski.bartek.supplib.alarm_management.GUI;

import android.os.Bundle;
import android.os.Message;

import pl.pawlowski.bartek.supplib.alarm_management.utilities.AbstractPausedActivityMessageHandler;
import pl.pawlowski.bartek.supplib.alarm_management.utilities.OnAlarmStateChangeListener;

/**
 * TODO javadoc
 */
public class AlarmReceiverForActivityProxy extends AbstractPausedActivityMessageHandler implements OnAlarmStateChangeListener {

    /** Identifies id of the alarm in internal bundle packages*/
    private static final String ALARM_ID = "id";

    /** Identifies time to fire of the alarm in internal bundle packages*/
    private static final String ALARM_FIRE_TIME = "time";

    /** Alarm scheduled indicator */
    public static final int ALARM_SCHEDULED = 0;

    /** Alarm canceled indicator */
    public static final int ALARM_CANCELED = 1;

    /** Alarm fired indicator */
    public static final int ALARM_FIRED = 2;

    /** Listener to be notified about alarm changes*/
    protected OnAlarmStateChangeListener alarmStateChangeListener;

    public AlarmReceiverForActivityProxy(OnAlarmStateChangeListener listener){
        alarmStateChangeListener = listener;
    }

    @Override
    protected void processMessage(Message msg) {
        if(alarmStateChangeListener != null){
            Bundle data = msg.getData();
            if(data == null){
                try{
                    throw new RuntimeException("Somehow data passed by message in "+getClass().getSimpleName()+" is NULL");
                }catch(RuntimeException e){
                    e.printStackTrace();
                }
            }else{
                int alarmId = data.getInt(ALARM_ID, -1);

                if(alarmId < 0){
                    //TODO jakis log
                }else{
                    switch (msg.what) {
                        case ALARM_SCHEDULED: {
                            long timeInFuture = data.getLong(ALARM_FIRE_TIME, -1);
                            if(timeInFuture < 0){
                                //TODO jakis log
                            }else{
                                alarmStateChangeListener.onAlarmScheduled(alarmId, 0l);
                            }
                            break;
                        }
                        case ALARM_CANCELED: {
                            alarmStateChangeListener.onAlarmCancelled(alarmId);
                            break;
                        }
                        case ALARM_FIRED: {
                            alarmStateChangeListener.onAlarmFired(alarmId);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onAlarmScheduled(int alarmId, long timeInFuture) {
        Bundle data = new Bundle();
        data.putInt(ALARM_ID, alarmId);
        data.putLong(ALARM_FIRE_TIME, timeInFuture);

        Message m = new Message();
        m.what = ALARM_SCHEDULED;
        m.setData(data);

        sendMessage(m);
    }

    @Override
    public void onAlarmCancelled(int alarmId) {
        Bundle data = new Bundle();
        data.putInt(ALARM_ID, alarmId);

        Message m = new Message();
        m.what = ALARM_CANCELED;
        m.setData(data);

        sendMessage(m);
    }

    @Override
    public void onAlarmFired(int alarmId) {
        Bundle data = new Bundle();
        data.putInt(ALARM_ID, alarmId);

        Message m = new Message();
        m.what = ALARM_FIRED;
        m.setData(data);

        sendMessage(m);
    }
}
