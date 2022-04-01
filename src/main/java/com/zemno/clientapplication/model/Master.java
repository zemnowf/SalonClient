package com.zemno.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Master implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    public Master(String name) {
        this.name = name;
    }
}
