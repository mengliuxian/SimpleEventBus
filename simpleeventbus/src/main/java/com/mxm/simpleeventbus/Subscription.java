package com.mxm.simpleeventbus;

import com.mxm.simpleeventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * <p>
 * 订阅者对�?包含订阅者和目标方法
 * <p>
 * Created by Administrator on 2018/10/17.
 */

public class Subscription {

    /**
     * 订阅者对�?
     */
    public WeakReference<Object> subscriber;

    /**
     * 订阅者的执行方法
     */
    public Method targetMethod;

    /**
     * 实现执行的线程模�?
     */
    public ThreadMode threadMode;

    /**
     * 事件类型
     */
    public EventType eventType;


    public Subscription(Object subscriber, SubscribeMode subscribeMode) {
        this.subscriber = new WeakReference<Object>(subscriber);
        this.targetMethod = subscribeMode.method;
        this.threadMode = subscribeMode.threadMode;
        this.eventType = subscribeMode.eventType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (subscriber != null ? !subscriber.equals(that.subscriber) : that.subscriber != null)
            return false;
        if (targetMethod != null ? !targetMethod.equals(that.targetMethod) : that.targetMethod != null)
            return false;
        if (threadMode != that.threadMode) return false;
        return eventType != null ? eventType.equals(that.eventType) : that.eventType == null;
    }

    @Override
    public int hashCode() {
        int result = subscriber != null ? subscriber.hashCode() : 0;
        result = 31 * result + (targetMethod != null ? targetMethod.hashCode() : 0);
        result = 31 * result + (threadMode != null ? threadMode.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        return result;
    }
}
