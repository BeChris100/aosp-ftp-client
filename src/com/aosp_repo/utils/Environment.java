package com.aosp_repo.utils;

public class Environment {

    private final String name, value;

    private Environment(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
