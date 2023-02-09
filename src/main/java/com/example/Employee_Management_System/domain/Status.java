package com.example.Employee_Management_System.domain;

public enum Status {

    DONE, IN_PROGRESS, NEW;

    public static Status from(String val) {
        switch (val) {
            case "DONE" -> {
                return DONE;
            }
            case "IN_PROGRESS" -> {
                return IN_PROGRESS;
            }
            case "NEW" -> {
                return NEW;
            }
            default -> {
                return null;
            }
        }
    }
}
