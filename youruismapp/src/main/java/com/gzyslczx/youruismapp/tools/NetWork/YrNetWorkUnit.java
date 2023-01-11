package com.gzyslczx.youruismapp.tools.NetWork;

import com.gzyslczx.youruismapp.requestes.TokenReqBody;
import com.gzyslczx.youruismapp.responses.TokenRes;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface YrNetWorkUnit {

    @POST(YrNetWorkPath.YrToken)
    public Observable<TokenRes> ReqToken(@Body TokenReqBody body);

}
