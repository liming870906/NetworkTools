package com.network.lm.common.eventbus;

import android.os.Bundle;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;


/**
 * Created by liming on 2018/3/1.
 */

public class BaseEvent {

    ///事件类型
    public enum EventType {
        StatisticJoinAgain(0xFF0001);//数据统计需要重新join的通知


        private final Integer value;

        EventType(final Integer value) {
            this.value = value;
        }

        @Override
        @NotNull
        public String toString() {
            return this.value.toString();
        }

        public Integer getValue() {
            return this.value;
        }
    }

    public int what;//类型
    public int arg1;
    public int arg2;
    public Object obj;

    private Bundle data;

    public Bundle getData() {
        if (data == null) {
            data = new Bundle();
        }

        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public Bundle peekData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return " event { what = " + what + " , arg1 = " + arg1 + "}";
    }
}
