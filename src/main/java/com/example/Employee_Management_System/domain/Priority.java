package com.example.Employee_Management_System.domain;

public enum Priority {

    LOW, MEDIUM, HIGH;

    public static Priority from(String val) {
        switch (val) {
            case "LOW" -> {
                return LOW;
            }
            case "MEDIUM" -> {
                return MEDIUM;
            }
            case "HIGH" -> {
                return HIGH;
            }
            default -> {
                return null;
            }
        }
    }

}
