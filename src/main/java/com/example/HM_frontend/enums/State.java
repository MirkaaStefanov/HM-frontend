package com.example.HM_frontend.enums;

public enum State {
    SALE("За продажба"),
    RENT("Дадено под наем"),
    NEW("Ново строителство"); // Example, add as needed

    private final String displayText;

    State(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }
}
