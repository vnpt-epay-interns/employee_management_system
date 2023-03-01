package com.example.Employee_Management_System.domain;

public enum Status {

    DONE, IN_PROGRESS, NEW, READY_FOR_REVIEW;

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
            case "READY_FOR_REVIEW" -> {
                return READY_FOR_REVIEW;
            }
            default -> {
                return null;
            }
        }
    }
}
