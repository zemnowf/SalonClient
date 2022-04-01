package com.zemno.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServiceName implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    public ServiceName(String name) {
        this.name = name;
    }
}
