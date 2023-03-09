package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {

    private static final String LOG_TAG = "BackgroundTask";
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    /**
     * Message handler that will receive task results.
     */
    protected Handler messageHandler;

    public BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }


    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }

    private void sendFailedMessage(String message) {
        Bundle msgBundle = createBundle(false);

        msgBundle.putString(MESSAGE_KEY, message);

        sendMessage(msgBundle);
    }

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    @NonNull
    private Bundle createBundle(boolean value) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, value);
        return msgBundle;
    }

    private void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = createBundle(false);

        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        sendMessage(msgBundle);
    }

    private void sendSuccessMessage() {
        Bundle msgBundle = createBundle(true);

        loadSuccessBundle(msgBundle);

        sendMessage(msgBundle);
    }

    @Override
    public void run() {
        try {
            processTask();

            sendSuccessMessage();

        } catch (Exception ex) {
            Log.e(LOG_TAG, "Failed", ex);
            sendExceptionMessage(ex);
        }
    }

    protected abstract void processTask();

    protected abstract void loadSuccessBundle(Bundle msgBundle);


}
