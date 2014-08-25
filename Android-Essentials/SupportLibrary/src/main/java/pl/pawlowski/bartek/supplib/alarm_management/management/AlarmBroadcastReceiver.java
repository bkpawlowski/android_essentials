package pl.pawlowski.bartek.supplib.alarm_management.management;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by bartek-mint on 14.02.14.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmController alarmController = AlarmController.getInstance(context);
        int alarmId = intent.getIntExtra(ExtendedAlarmManager.ALARM_IDENTIFIER, -1);

        if(alarmId > 0){
            alarmController.onAlarmFired(alarmId);
        }else{
            //TODO jakas notyfikacja
        }
    }
}
