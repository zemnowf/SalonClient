package com.zemno.clientapplication.repository;

import com.zemno.clientapplication.controller.Controller;
import com.zemno.clientapplication.model.ServiceName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceNameRepo extends Controller {

    private ObservableList<ServiceName> serviceNameDataProd = FXCollections.observableArrayList();
    private List<ServiceName> serviceNameListProd = new ArrayList<>();
    private ResponseEntity<List<ServiceName>> serviceNameData;

    public ObservableList<ServiceName> getServiceNameDataProd() {
        return serviceNameDataProd;
    }

    public List<ServiceName> getServiceNameListProd() {
        return serviceNameListProd;
    }

    public void init(){
        serviceNameDataProd.clear();
        serviceNameData = restClient.exchange(SERVER_URL + "serviceNames",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ServiceName>>(){});
        serviceNameData.getBody().forEach(p -> {
            serviceNameDataProd.add(p);
            serviceNameListProd.add(p);
        });
    }
}
