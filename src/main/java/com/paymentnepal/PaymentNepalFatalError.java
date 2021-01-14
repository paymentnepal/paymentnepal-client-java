package com.paymentnepal;


public class PaymentNepalFatalError extends Exception {
    private static final long serialVersionUID = -3979667104002126209L;
    PaymentNepalErrorCode code;

    public PaymentNepalErrorCode getCode() {
        return code;
    }

    public void setCode(PaymentNepalErrorCode code) {
        this.code = code;
    }

    public PaymentNepalFatalError(String s) {
        super(s);
        setCode(PaymentNepalErrorCode.COMMON);
    }

    public PaymentNepalFatalError(String s, PaymentNepalErrorCode code) {
        super(s);
        this.code = code;
    }
}
