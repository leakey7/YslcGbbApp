package com.gzyslczx.yslc.tools;

public interface OnVideoPlayState {

    void OnStop();
    void OnStart();
    void OnReStart();

    void OnFullScreen();
    void OnSmallScreen();

    void ChangeProgress(int progress);

}
