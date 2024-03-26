package com.everyhelpcounts.web.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortalException extends RuntimeException {

    long code;

    String description;

    String userDescription;

    public PortalException() {

    }

    public PortalException(long code, String description, String userDescription) {
        this.code = code;
        this.description = description;
        this.userDescription = userDescription;
    }

    @Override
    public String toString() {
        return "PortalException{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", userDescription='" + userDescription + '\'' +
                '}';
    }
}
