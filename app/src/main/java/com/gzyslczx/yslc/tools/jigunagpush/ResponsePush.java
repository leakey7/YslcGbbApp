package com.gzyslczx.yslc.tools.jigunagpush;

public class ResponsePush {

    private String msg_id, n_title, n_content;
    private PushExtras n_extras;
    private byte rom_type;

    public String getMsg_id() {
        return msg_id;
    }

    public String getN_title() {
        return n_title;
    }

    public String getN_content() {
        return n_content;
    }

    public PushExtras getN_extras() {
        return n_extras;
    }

    public byte getRom_type() {
        return rom_type;
    }
}
