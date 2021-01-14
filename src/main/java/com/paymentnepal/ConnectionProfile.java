package com.paymentnepal;

/**
 * Профиль подключения
 */
public class ConnectionProfile {
    protected String baseUrl;
    protected String cardTokenUrl;
    protected String cardTokenTestUrl;

    public ConnectionProfile(String baseUrl, String cardTokenUrl, String cardTokenTestUrl) {
        this.baseUrl = baseUrl;
        this.cardTokenUrl = cardTokenUrl;
        this.cardTokenTestUrl = cardTokenTestUrl;
    }

    static public ConnectionProfile first() {
        return new ConnectionProfile(
                "https://pay.paymentnepal.com/",
                "https://secure.paymentnepal.com/cardtoken/",
                "https://test.paymentnepal.com/cardtoken/"
        );
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCardTokenUrl() {
        return cardTokenUrl;
    }

    public void setCardTokenUrl(String cardTokenUrl) {
        this.cardTokenUrl = cardTokenUrl;
    }

    public String getCardTokenTestUrl() {
        return cardTokenTestUrl;
    }

    public void setCardTokenTestUrl(String cardTokenTestUrl) {
        this.cardTokenTestUrl = cardTokenTestUrl;
    }
}
