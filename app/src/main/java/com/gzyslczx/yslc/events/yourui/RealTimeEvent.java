package com.gzyslczx.yslc.events.yourui;

import com.yourui.sdk.message.use.Realtime;

import java.util.List;

public class RealTimeEvent {
    List<Realtime> realtimeList;

    public RealTimeEvent(List<Realtime> realtimeList) {
        this.realtimeList = realtimeList;
    }

    public List<Realtime> getRealtimeList() {
        return realtimeList;
    }
}
