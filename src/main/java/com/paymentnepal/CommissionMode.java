package com.paymentnepal;

/**
 * На ком лежит комиссия
 */
public enum CommissionMode {
    PARTNER("partner"),
    ABONENT("abonent")
    ;

    private final String text;

    CommissionMode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
