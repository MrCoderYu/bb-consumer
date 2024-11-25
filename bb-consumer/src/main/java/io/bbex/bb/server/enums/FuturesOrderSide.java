package io.bbex.bb.server.enums;

import io.bbex.base.proto.OrderSideEnum;

public enum FuturesOrderSide {

    UNKNOWN(0),

    BUY_OPEN(1),

    SELL_OPEN(2),

    BUY_CLOSE(3),

    SELL_CLOSE(4);

    private int value;

    FuturesOrderSide(int value) {
        this.value = value;
    }

    public static FuturesOrderSide getSide(OrderSideEnum sideEnum, boolean isClose) {
        if (sideEnum == OrderSideEnum.BUY) {
            return isClose ? BUY_CLOSE : BUY_OPEN;
        }

        if (sideEnum == OrderSideEnum.SELL) {
            return isClose ? SELL_CLOSE : SELL_OPEN;
        }
        return null;
    }

}
