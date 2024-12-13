package com.github.lipinskipawel.common.transaction;

public enum Type {
    BUY,
    SELL;

    public static Type type(String type) {
        return Type.valueOf(type);
    }
}
