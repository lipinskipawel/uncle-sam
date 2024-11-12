package com.github.lipinskipawel.broker;

public enum Type {
    BUY,
    SELL;

    public static Type type(String type) {
        return Type.valueOf(type);
    }
}
