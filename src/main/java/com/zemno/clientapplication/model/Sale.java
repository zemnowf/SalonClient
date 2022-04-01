package com.zemno.clientapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Sale implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("service_name")
    private String serviceName;
    @JsonProperty("service_type")
    private String serviceType;
    @JsonProperty("sale_detail")
    private String saleDetail;
    @JsonProperty("total_cost")
    private Double totalCost;
    @JsonProperty("master_name")
    private String masterName;

    public Sale(String serviceName, String serviceType, String saleDetail, Double totalCost, String masterName) {
        this.date = LocalDate.now();
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.saleDetail = saleDetail;
        this.totalCost = totalCost;
        this.masterName = masterName;
    }
}
