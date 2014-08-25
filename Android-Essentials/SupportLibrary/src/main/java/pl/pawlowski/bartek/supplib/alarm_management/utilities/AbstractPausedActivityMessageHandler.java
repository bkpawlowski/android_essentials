package pl.pawlowski.bartek.supplib.alarm_management.utilities;

import android.os.Handler;
import android.os.Message;

import java.util.Vector;

/**
 * Extended {@link Handler} class to be use when activity is paused and resumed
 *
 * @author Garet
 */
public abstract class AbstractPausedActivityMessageHandler extends Handler {

    /** Determines whether activity is paused or not */
    protected boolean isActivityPaused;

    /** Awaiting message queue */
    protected Vector<Message> messageQueue;

    /** Default constructor*/
    protected AbstractPausedActivityMessageHandler(){
        isActivityPaused = true;
        messageQueue = new Vector<Message>();
    }

    @Override
    final public void handleMessage(Message msg) {
        if (isActivityPaused) {
			 /*if activity is paused, adding message to activity resume await
			 queue*/
            Message msgCpy = new Message();
            msgCpy.copyFrom(msg);
            messageQueue.add(msgCpy);

        } else {
			/* else we process the message */
            processMessage(msg);
        }
    }

    /**
     * Abstract method to implement independend processing mechanism
     *
     * @param msg
     *            - received {@link Message} object
     */
    protected abstract void processMessage(Message msg);

    /**
     * Sends messages again when onResume callback triggers in activity
     */
    public final void onActivityResumed() {
        isActivityPaused = false;

		/* sending messages from queue again to handle */
        while (messageQueue.size() > 0) {
            sendMessage(messageQueue.remove(0));
        }
    }

    /**
     * Should be called when onPause callback triggers in activity
     */
    public final void onActivityPaused() {
        isActivityPaused = true;
    }

    public final boolean isActivityPaused(){
        return isActivityPaused;
    }
}
