package com.zemno.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ServiceUnit implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("service_unit_cost")
    private Double serviceUnitCost;
    @JsonProperty("type")
    private String type;
    @JsonProperty("master")
    private Master master;
    @JsonProperty("serviceName")
    private ServiceName serviceName;

    public ServiceUnit(String detail, Double serviceUnitCost, String type, Master master, ServiceName serviceName) {
        this.detail = detail;
        this.serviceUnitCost = serviceUnitCost;
        this.type = type;
        this.master = master;
        this.serviceName = serviceName;
    }
}
