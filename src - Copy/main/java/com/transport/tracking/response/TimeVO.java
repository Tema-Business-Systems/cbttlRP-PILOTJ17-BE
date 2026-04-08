package com.transport.tracking.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeVO {

    private String value;
    private String label;

    @Override
    public String toString() {
        return "TimeVO{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
