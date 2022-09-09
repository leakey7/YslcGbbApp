package com.gzyslczx.yslc.events;

import com.gzyslczx.yslc.modes.response.ResDGSCard;

public class NoticeChangeSpecialSupCardEvent {

    private int CardCode;
    private ResDGSCard DateCard;

    public NoticeChangeSpecialSupCardEvent() {
    }

    public void setCardCode(int cardCode) {
        CardCode = cardCode;
    }

    public void setDateCard(ResDGSCard dateCard) {
        DateCard = dateCard;
    }

    public int getCardCode() {
        return CardCode;
    }

    public ResDGSCard getDateCard() {
        return DateCard;
    }
}
