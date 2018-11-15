package com.mxm.simpleeventbus.handler;

import com.mxm.simpleeventbus.Subscription;

import java.lang.reflect.InvocationTargetException;

/**
 * 默认线程，事件的抛出线程
 * <p>
 * Created by Administrator on 2018/10/18.
 */

public class DefEventHandler implements IEventHandler {
    @Override
    public void handlerEvnt(Subscription subscription, Object event) {
        if (subscription == null || subscription.subscriber.get() == null) {
            return;
        }
        try {

            subscription.targetMethod.invoke(subscription.subscriber.get(), event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}
