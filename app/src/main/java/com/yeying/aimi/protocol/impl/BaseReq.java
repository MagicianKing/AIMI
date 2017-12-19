package com.yeying.aimi.protocol.impl;

import java.io.Serializable;

public class BaseReq implements Serializable {

    public String transcode;
    public String sessionId;
    public Header header = new Header();

    public String getTranscode() {
        return transcode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}
