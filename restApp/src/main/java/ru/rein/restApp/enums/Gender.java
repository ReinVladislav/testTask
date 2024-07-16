package ru.rein.restApp.enums;

import lombok.Getter;

@Getter
public enum Gender {
    MAN("MAN"),
    WOMAN("WOMAN");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
