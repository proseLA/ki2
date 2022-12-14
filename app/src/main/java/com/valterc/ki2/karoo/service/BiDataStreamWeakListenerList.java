package com.valterc.ki2.karoo.service;

import android.annotation.SuppressLint;
import android.util.Log;

import com.valterc.ki2.retro.BiConsumer;

import java.util.WeakHashMap;

@SuppressLint("LogNotTimber")
public class BiDataStreamWeakListenerList<TData1, TData2> {

    private final WeakHashMap<BiConsumer<TData1, TData2>, Boolean> listeners;
    private TData1 lastData1;
    private TData2 lastData2;

    public BiDataStreamWeakListenerList() {
        this.listeners = new WeakHashMap<>();
    }

    public boolean hasListeners() {
        return listeners.size() != 0;
    }

    public void addListener(BiConsumer<TData1, TData2> consumer) {
        if (consumer == null) {
            return;
        }

        listeners.put(consumer, null);

        if (lastData1 != null || lastData2 != null) {
            consumer.accept(lastData1, lastData2);
        }
    }

    public void pushData(TData1 data1, TData2 data2) {
        pushData(data1, data2, true);
    }

    public void pushData(TData1 data1, TData2 data2, boolean keepData) {
        if (keepData) {
            lastData1 = data1;
            lastData2 = data2;
        }

        for (BiConsumer<TData1, TData2> c : listeners.keySet()) {
            try {
                c.accept(data1, data2);
            } catch (Exception e) {
                Log.e("KI2", "Error during callback", e);
            }
        }
    }
}
