package com.derekgelvez.lawfirmcommon.event;

public class ClientRegisteredEvent {

    private final Long userId;

    public ClientRegisteredEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}