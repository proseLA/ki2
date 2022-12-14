package com.valterc.ki2.karoo.formatters;

import androidx.annotation.NonNull;

import com.valterc.ki2.data.device.DeviceId;
import com.valterc.ki2.data.shifting.ShiftingInfo;
import com.valterc.ki2.karoo.Ki2Context;
import com.valterc.ki2.retro.BiConsumer;

import io.hammerhead.sdk.v0.datatype.formatter.SdkFormatter;

public class ShiftCountTextFormatter extends SdkFormatter {

    private ShiftingInfo lastShiftingInfo;
    private int shiftCount;

    @SuppressWarnings("FieldCanBeLocal")
    private final BiConsumer<DeviceId, ShiftingInfo> shiftingInfoConsumer = (deviceId, shiftingInfo) -> {
        if (shiftingInfo == null) {
            return;
        }

        if (lastShiftingInfo == null){
            lastShiftingInfo = shiftingInfo;
            return;
        }

        shiftCount += Math.abs(lastShiftingInfo.getFrontGear() - shiftingInfo.getFrontGear());
        shiftCount += Math.abs(lastShiftingInfo.getRearGear() - shiftingInfo.getRearGear());

        lastShiftingInfo = shiftingInfo;
    };

    public ShiftCountTextFormatter(Ki2Context ki2Context) {
        ki2Context.getServiceClient().registerShiftingInfoWeakListener(shiftingInfoConsumer);
    }

    @NonNull
    @Override
    public String formatValue(double value) {
        return Integer.toString(shiftCount);
    }
}