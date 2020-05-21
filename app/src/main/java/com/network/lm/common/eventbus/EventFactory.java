package com.network.lm.common.eventbus;

/**
 * Created by liming on 2018/3/1.
 *
 * @author liming
 * EventBus时间工厂
 */
public abstract class EventFactory {
    /**
     * 抽象工厂方法
     *
     * @param clz 产品对象类型
     * @param <T>
     * @return 具体产品对象
     */
    public abstract <T extends BaseEvent> T createEvent(Class<T> clz);
}
