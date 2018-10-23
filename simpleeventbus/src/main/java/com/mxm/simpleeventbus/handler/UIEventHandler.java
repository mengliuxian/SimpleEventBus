package com.mxm.simpleeventbus.handler;

import android.os.Handler;
import android.os.Looper;

import com.mxm.simpleeventbus.Subscription;


import java.util.logging.LogRecord;

/**
 * 主线程执行
 * <p>
 * Created by Administrator on 2018/10/18.
 */

public class UIEventHandler implements IEventHandler {

    private IEventHandler defHandler = new DefEventHandler();


    private Handler mUIHandler = new Handler(Looper.getMainLooper());


    @Override
    public void handlerEvnt(final Subscription subscription, final Object event) {

        mUIHandler.post(new Runnable() {
            @Override
            public void run() {

                defHandler.handlerEvnt(subscription,event);
            }
        });

    }
}
