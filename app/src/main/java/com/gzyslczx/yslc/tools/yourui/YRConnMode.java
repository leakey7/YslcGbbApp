package com.gzyslczx.yslc.tools.yourui;

import com.gzyslczx.yslc.modes.yourui.ReqToken;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface YRConnMode {

    @POST(YRConnUnit.TokenConn)
    Observable<YRTokenRes> RequestToken(@Header(YRConnUnit.ORIGIN) String origin, @Body ReqToken req);

}
