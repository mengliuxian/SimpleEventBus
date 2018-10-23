package com.mxm.simpleeventbus;

import android.util.Log;

import com.mxm.simpleeventbus.handler.DefEventHandler;
import com.mxm.simpleeventbus.handler.IEventHandler;
import com.mxm.simpleeventbus.handler.NewThreadEventHandler;
import com.mxm.simpleeventbus.handler.UIEventHandler;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018/10/17.
 */

public class EventBus {


    /**
     * <p>
     * 保存订阅者的{@link Map}
     * {@link CopyOnWriteArrayList}可以在操作{@link List}的时候,继续添加新的数据而不会报错
     * <p>
     */
    private final Map<EventType, CopyOnWriteArrayList<Subscription>> mSubscriberMap =
            new ConcurrentHashMap<>();


    /**
     * 保存粘性事件的{@link LinkedList}
     */
    private List<EventType> mStickyEvents = Collections.synchronizedList(new LinkedList<EventType>());


    /**
     * {@link ThreadLocal} 表示线程的本地变量，所有线程共享 {@link ThreadLocal}，但是不通的线程，取到的值不同
     */
    private ThreadLocal<Queue<EventType>> mLocalEvents = new ThreadLocal<Queue<EventType>>() {
        @Override
        protected Queue<EventType> initialValue() {
            return new ConcurrentLinkedQueue<EventType>();
        }
    };


    private EventDispatcher dispatcher = new EventDispatcher();

    private SubscribeManage subscribeManage = new SubscribeManage(mSubscriberMap);

    private static class EventBusBuilder {
        public static EventBus eventBus = new EventBus();
    }


    public static EventBus newInstence() {
        return EventBusBuilder.eventBus;

    }

    private EventBus() {

    }


    /**
     * 注册
     *
     * @param subscriber
     */
    public void register(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        synchronized (this) {
            subscribeManage.findSubscribeMethod(subscriber);
        }
    }


    /**
     * 取消注册
     *
     * @param subscriber
     */
    public void unregister(Object subscriber) {
        if (subscriber == null) {
            return;
        }
        synchronized (this) {
            subscribeManage.removeMethodForMap(subscriber);
        }
    }


    /**
     * post a event
     *
     * @param event
     */
    public void post(Object event) {
        post(event, EventType.DEFAULT_TAG);
    }

    /**
     * 发布事件
     *
     * @param event 要发布的事件
     * @param tag   事件的tag, 类似于BroadcastReceiver的action
     */
    public void post(Object event, String tag) {
        if (event == null) {
            Log.e(this.getClass().getSimpleName(), "The event object is null");
            return;
        }

        mLocalEvents.get().offer(new EventType(event.getClass(), tag));
        dispatcher.dispatchEvents(event);
    }

    public class EventDispatcher {

        /**
         * 在主线程执行
         */
        IEventHandler mUIhandler = new UIEventHandler();

        /**
         * 在新线程执行
         */
        IEventHandler mNewThreadHandler = new NewThreadEventHandler();

        /**
         * 在原线程执行
         */
        IEventHandler mDefHandler = new DefEventHandler();


        public void dispatchEvents(Object event) {
            Queue<EventType> eventTypes = mLocalEvents.get();
            while (eventTypes.size() > 0) {
                handleEvent(eventTypes.poll(), event);
            }

        }

        private void handleEvent(EventType eventType, Object aEvent) {

            CopyOnWriteArrayList<Subscription> subscriptions = mSubscriberMap.get(eventType);
            if (subscriptions == null) {
                return;
            }
            for (Subscription subscription : subscriptions) {
                ThreadMode threadMode = subscription.threadMode;
                IEventHandler eventHandler = getEventHandler(threadMode);
                eventHandler.handlerEvnt(subscription, aEvent);

            }


        }


        private IEventHandler getEventHandler(ThreadMode mode) {
            if (mode == ThreadMode.NEWTHREAD) {
                return mNewThreadHandler;
            }
            if (mode == ThreadMode.RUN) {
                return mDefHandler;
            }
            return mUIhandler;
        }
    }


}
