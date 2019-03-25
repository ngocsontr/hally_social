package com.hally.influencerai.enums;

public enum UserType {
    FACEBOOK(1),
    TWITTER(2),
    INSTAGRAM(3),
    YOUTUBE(4),
    OTHER(0);

    public final int value;

    UserType(int value) {
        this.value = value;
    }
}
