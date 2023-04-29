package com.sgp.gdsc_hackathon.user;

import lombok.Getter;

import javax.persistence.*;

@Entity(name = "member")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }
}
