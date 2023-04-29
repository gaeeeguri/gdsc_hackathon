package com.sgp.gdsc_hackathon.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class UserForm {
    @NotEmpty
    private String username;
}
