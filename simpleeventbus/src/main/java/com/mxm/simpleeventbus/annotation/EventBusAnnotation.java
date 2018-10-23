package com.mxm.simpleeventbus.annotation;

import com.mxm.simpleeventbus.EventType;
import com.mxm.simpleeventbus.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注册类的方法注解
 * <p>
 * <p>
 * Created by Administrator on 2018/10/17.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface EventBusAnnotation {

    /**
     * 用于区分接收事件的标记
     *
     * @return
     */
    String tag() default EventType.DEFAULT_TAG;

    /**
     * 事件执行的线程，默认在主线程
     *
     * @return
     */
    ThreadMode mode() default ThreadMode.MAIN;

}
