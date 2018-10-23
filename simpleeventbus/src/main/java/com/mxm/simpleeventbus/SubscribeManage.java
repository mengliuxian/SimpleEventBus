package com.mxm.simpleeventbus;

import android.util.Log;

import com.mxm.simpleeventbus.annotation.EventBusAnnotation;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用于获取注册对象的内部方法
 * <p>
 * Created by Administrator on 2018/10/18.
 */

public class SubscribeManage {

    /**
     * 保存注册者信息的键值对
     */
    Map<EventType, CopyOnWriteArrayList<Subscription>> mSubscriberMap;

    public SubscribeManage(Map<EventType, CopyOnWriteArrayList<Subscription>> mSubscriberMap) {
        this.mSubscriberMap = mSubscriberMap;
    }

    /**
     * 查找订阅对象中的所有订阅函数,订阅函数的参数只能有一个.找到订阅函数之后构建Subscription存储到Map中
     *
     * @param subscriber
     */
    public void findSubscribeMethod(Object subscriber) {

        if (subscriber != null) {

            Class<?> clazz = subscriber.getClass();
            // 查找类中符合要求的注册方法,直到Object类
            while (clazz != null && !isSystemCalss(clazz.getName())) {
                final Method[] allMethods = clazz.getDeclaredMethods();
                for (int i = 0; i < allMethods.length; i++) {
                    Method method = allMethods[i];
                    // 根据注解来解析函数
                    EventBusAnnotation annotation = method.getAnnotation(EventBusAnnotation.class);
                    if (annotation != null) {
                        // 获取方法参数
                        Class<?>[] paramsTypeClass = method.getParameterTypes();
                        // 订阅函数只支持一个参数
                        if (paramsTypeClass != null && paramsTypeClass.length == 1) {
                            Class<?> paramType = convertType(paramsTypeClass[0]);
                            EventType eventType = new EventType(paramType, annotation.tag());
                            SubscribeMode subscribeMethod = new SubscribeMode(method, eventType,
                                    annotation.mode());
                            putSubscibe(eventType, subscribeMethod, subscriber);
                        }
                    }
                } // end for
                // 获取父类,以继续查找父类中符合要求的方法
                clazz = clazz.getSuperclass();
            }

        } else {
            Log.d("SimpleEventBus", "SubscribeManage : findSubscribeMethod: the mSubcriberMap is null");
        }

    }

    /**
     * 按照EventType存储订阅者列表,这里的EventType就是事件类型,一个事件对应0到多个订阅者.
     *
     * @param event      事件
     * @param method     订阅方法对象
     * @param subscriber 订阅者
     */
    private void putSubscibe(EventType event, SubscribeMode method, Object subscriber) {
        CopyOnWriteArrayList<Subscription> subscriptionLists = mSubscriberMap.get(event);
        if (subscriptionLists == null) {
            subscriptionLists = new CopyOnWriteArrayList<Subscription>();
        }

        Subscription newSubscription = new Subscription(subscriber, method);
        if (subscriptionLists.contains(newSubscription)) {
            return;
        }

        subscriptionLists.add(newSubscription);
        // 将事件类型key和订阅者信息存储到map中
        mSubscriberMap.put(event, subscriptionLists);
    }


    /**
     * 在Map中去掉注册对象
     *
     * @param subscriber
     */
    public void removeMethodForMap(Object subscriber) {

        Iterator<CopyOnWriteArrayList<Subscription>> iterator = mSubscriberMap.values().iterator();
        while (iterator.hasNext()) {
            CopyOnWriteArrayList<Subscription> subscriptions = iterator.next();
            if (subscriptions != null) {
                List<Subscription> foundSubscriptions = new
                        LinkedList<Subscription>();
                Iterator<Subscription> subIterator = subscriptions.iterator();
                while (subIterator.hasNext()) {
                    Subscription subscription = subIterator.next();
                    //在弱引用中获取对象
                    Object cacheObject = subscription.subscriber.get();
                    if (isObjectsEqual(cacheObject, subscriber) || cacheObject != null) {
                        Log.d("SimpleEventBus", "SubscribeManage : removeMethodForMap: 移除订阅" + subscriber.getClass().getName());
                        foundSubscriptions.add(subscription);

                    }
                }
                // 移除该subscriber的相关的Subscription
                subscriptions.removeAll(foundSubscriptions);
            }
            // 如果针对某个Event的订阅者数量为空了,那么需要从map中清除
            if (subscriptions == null || subscriptions.size() == 0) {
                iterator.remove();
            }
        }

    }

    private boolean isObjectsEqual(Object cachedObj, Object subscriber) {
        return cachedObj != null
                && cachedObj.equals(subscriber);
    }

    /**
     * 把基本数据类型转为包装类
     *
     * @param eventType origin Event Type
     * @return
     */
    private Class<?> convertType(Class<?> eventType) {
        Class<?> returnClass = eventType;
        if (eventType.equals(boolean.class)) {
            returnClass = Boolean.class;
        } else if (eventType.equals(int.class)) {
            returnClass = Integer.class;
        } else if (eventType.equals(float.class)) {
            returnClass = Float.class;
        } else if (eventType.equals(double.class)) {
            returnClass = Double.class;
        }

        return returnClass;
    }

    private boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }

}
