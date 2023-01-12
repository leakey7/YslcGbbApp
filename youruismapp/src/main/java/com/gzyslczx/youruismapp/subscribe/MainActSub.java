package com.gzyslczx.youruismapp.subscribe;

import com.gzyslczx.youruismapp.events.UpdateYRTokenEvent;

import org.greenrobot.eventbus.Subscribe;

public interface MainActSub {

    @Subscribe
    void onUpdateYRToken(UpdateYRTokenEvent event);
}
