package com.mxm.simpleeventbus;

/**
 *
 * 本类用于作为Map的Key，可以用来查找所有注册了相同类型和tag的订阅者，可以在接到消息时调用所有的
 *
 *
 * Created by Administrator on 2018/10/17.
 */

public class EventType {

    /**
     * 默认tag
     */
    public static final String DEFAULT_TAG = "default_tag";

    public Class<?> aClass;

    public String tag = DEFAULT_TAG;

    public Object event;

    public EventType(Class<?> aClass) {
        this(aClass, DEFAULT_TAG);
    }

    public EventType(Class<?> aClass, String tag) {
        this.aClass = aClass;
        this.tag = tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        if (aClass != null ? !aClass.equals(eventType.aClass) : eventType.aClass != null)
            return false;
        return tag != null ? tag.equals(eventType.tag) : eventType.tag == null;
    }

    @Override
    public int hashCode() {
        int result = aClass != null ? aClass.hashCode() : 0;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        return result;
    }
}
