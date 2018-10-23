package com.mxm.simpleeventbus.handler;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.mxm.simpleeventbus.Subscription;

/**
 * Created by Administrator on 2018/10/18.
 */

public class NewThreadEventHandler implements IEventHandler {

    private IEventHandler defHandler = new DefEventHandler();

    private NewHandlerThread newHandlerThread = new NewHandlerThread(NewThreadEventHandler.class.getName());

    @Override
    public void handlerEvnt(final Subscription subscription, final Object event) {
        newHandlerThread.start();
        newHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                defHandler.handlerEvnt(subscription, event);
            }
        });


    }


    private class NewHandlerThread extends HandlerThread {

        private Handler handler;

        public NewHandlerThread(String name) {
            super(name);
        }

        @Override
        public synchronized void start() {
            super.start();
            handler = new Handler(getLooper());
        }

        public void post(Runnable runnable) {

            if (runnable != null) {

                handler.post(runnable);
            } else {
                Log.d("SimpleEventBus", "NewThreadEventHandler : post: the runnable is null");
            }


        }

    }


}
