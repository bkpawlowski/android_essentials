package pl.pawlowski.bartek.supplib.alarm_management.GUI;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import pl.pawlowski.bartek.supplib.alarm_management.management.AlarmController;
import pl.pawlowski.bartek.supplib.alarm_management.utilities.OnAlarmStateChangeListener;

public class AlarmStateChangeListenerFragment extends Fragment implements OnAlarmStateChangeListener{

    /**
     * Used when activity/fragment is in paused state.
     * Every incoming events are queued inside this handler
     * to be handled by GUIUpdater when activity/fragment is eventually in resumed state.
     */
    protected AlarmReceiverForActivityProxy alarmReceiverForActivityProxy;

    private boolean backButtonHandlingLocked;

    protected FragmentManager fragmentManager;

    public boolean isBackButtonHandlingLocked() {
        return backButtonHandlingLocked;
    }

    public void setBackButtonHandlingLocked(boolean backButtonHandlingLocked) {
        this.backButtonHandlingLocked = backButtonHandlingLocked;
    }

    /**
     * @return
     * instance of {@link AlarmReceiverForActivityProxy} that handles
     * incoming notifications about alarms state changes and delivers it to another handlers
     */
    protected AlarmReceiverForActivityProxy getAlarmReceiverForActivityProxy(){
        return new AlarmReceiverForActivityProxy(this);
    }

    /**
     * Default constructor
     */
    protected AlarmStateChangeListenerFragment(){
        alarmReceiverForActivityProxy = getAlarmReceiverForActivityProxy();
        backButtonHandlingLocked = false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof FragmentActivity){
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentManager = fragmentActivity.getSupportFragmentManager();
            fragmentManager.addOnBackStackChangedListener(new OnBackStackChangeListener());
        }else{
            throw new RuntimeException("EEEEE, ZIOMEG Z SUPPORT LIBRARY MASZ KORZYSTAC");
        }
    }

    /**
     *
     *
     *
     *
     *
     *
     * OBSLUZYC ZMIANE MENU DLA FRAGMENTU EDITORFRAGMENTXDXDXD
     *
     *
     *
     *
     *
     *
     *
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(alarmReceiverForActivityProxy == null){
            AlarmController ac = AlarmController.getInstance(getActivity());
            ac.addOnAlarmStateChangeListener(alarmReceiverForActivityProxy);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new OnBackButtonPressedListener(this));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(alarmReceiverForActivityProxy == null){
            alarmReceiverForActivityProxy.onActivityResumed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(alarmReceiverForActivityProxy == null){
            alarmReceiverForActivityProxy.onActivityPaused();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(alarmReceiverForActivityProxy == null){
            AlarmController ac = AlarmController.getInstance(getActivity());
            ac.removeOnAlarmStateChangeListener(alarmReceiverForActivityProxy);
        }
    }

    @Override
    public void onAlarmScheduled(int alarmId, long timeInFuture) {
        //do obslugi w podklasach
    }

    @Override
    public void onAlarmCancelled(int alarmId) {
        //do obslugi w podklasach
    }

    @Override
    public void onAlarmFired(int alarmId) {
        //do obslugi w podklasach
    }

    /**
     * Called when back button has been just pressed by the user
     */
    protected void onBackButtonPressed(){
        if(fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * Retrieves events about BackButton pressed and calls onBackButtonPressed method
     * on specified in constructor parameter ActionBarMenuFragment instance
     */
    class OnBackButtonPressedListener implements View.OnKeyListener {
        private AlarmStateChangeListenerFragment f;

        public OnBackButtonPressedListener(AlarmStateChangeListenerFragment fragment) {
            f = fragment;
        }

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

            if(keyEvent.getAction() == KeyEvent.ACTION_UP){
                /* by w aktywnosci otrzymac wywolanie OnBackPressed potrzebne sa obydwa eventy
                 * ACTON_UP i ACTION_DOWN dlatego, zadnego z nich nie mozna skonsumowac zwracajac TRUE
                 * a ja konsumuje i recznie wywoluje onBackPressed - smieszek poza kontrolo
                 */
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                f.onBackButtonPressed();
            }

            final boolean consumeEvent = AlarmStateChangeListenerFragment.this.isBackButtonHandlingLocked();

            if(!consumeEvent) {
                getActivity().onBackPressed();
            }

            return consumeEvent;
        }
    }

    private class OnBackStackChangeListener implements FragmentManager.OnBackStackChangedListener{
        @Override
        public void onBackStackChanged() {
            if(fragmentManager.getBackStackEntryCount() == 0){
                setBackButtonHandlingLocked(false);
            }else{
                setBackButtonHandlingLocked(true);
            }
        }
    }
}
