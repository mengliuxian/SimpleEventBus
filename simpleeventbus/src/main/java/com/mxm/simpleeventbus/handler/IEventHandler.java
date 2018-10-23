package com.mxm.simpleeventbus.handler;

import com.mxm.simpleeventbus.Subscription;

/**
 * 事件处理的接口
 * <p>
 * <p>
 * Created by Administrator on 2018/10/18.
 */

public interface IEventHandler {

    /**
     * 处理事件
     *
     * @param subscription 订阅对象
     * @param event        待处理的事件
     */
    void handlerEvnt(Subscription subscription, Object event);

}
