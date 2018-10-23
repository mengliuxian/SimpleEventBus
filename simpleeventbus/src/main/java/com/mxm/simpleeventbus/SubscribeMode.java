package com.mxm.simpleeventbus;

import com.mxm.simpleeventbus.ThreadMode;

import java.lang.reflect.Method;

/**
 * 订阅者的属性，包含了方法信息、参数名、执行的线程模式
 * <p>
 * <p>
 * Created by Administrator on 2018/10/17.
 */

public class SubscribeMode {

    /**
     * 订阅者执行的方法
     */
    public Method method;

    /**
     * 订阅者的参数类型
     */
    public EventType eventType;

    /**
     * 订阅者方法执行的线程
     */
    public ThreadMode threadMode;

    public SubscribeMode(Method method, EventType eventType, ThreadMode threadMode) {
        this.method = method;
        this.eventType = eventType;
        this.threadMode = threadMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubscribeMode that = (SubscribeMode) o;

        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null)
            return false;
        return threadMode == that.threadMode;
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (threadMode != null ? threadMode.hashCode() : 0);
        return result;
    }
}
