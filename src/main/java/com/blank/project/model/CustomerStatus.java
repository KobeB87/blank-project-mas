package com.blank.project.model;

public enum CustomerStatus {
    CREATED,
    UPDATED,
    DELETED;

    public String value() {
        return name();
    }

    public static CustomerStatus fromValue(final  String value) {
        return valueOf(value);
    }
}
