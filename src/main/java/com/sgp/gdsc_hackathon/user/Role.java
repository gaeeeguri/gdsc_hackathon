package com.sgp.gdsc_hackathon.user;


public enum Role {
    USER, ADMIN;

    public String getKey() {
        return name();
    }
}
