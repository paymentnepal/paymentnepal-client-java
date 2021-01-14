package com.paymentnepal;

/**
 * Коды ошибок
 */
public enum PaymentNepalErrorCode {
    TYPE("type"),
    AUTH("auth"),
    DATA("data"),
    COMMON("common"),
    UNIQUE("unique");

    private final String text;

    PaymentNepalErrorCode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static PaymentNepalErrorCode fromString(String text) {
        if (text != null) {
            for (PaymentNepalErrorCode code : PaymentNepalErrorCode.values()) {
                if (text.equalsIgnoreCase(code.text)) {
                    return code;
                }
            }
        }
        return COMMON;
    }
}
