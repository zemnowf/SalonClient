package com.zemno.clientapplication.repository;

import com.zemno.clientapplication.controller.Controller;
import com.zemno.clientapplication.model.ServiceUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceUnitRepo extends Controller {

    private ObservableList<ServiceUnit> serviceUnitDataProd = FXCollections.observableArrayList();
    private ResponseEntity<List<ServiceUnit>> serviceUnitData;
    private static Long selectedItemId;

    public Long getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Long selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public ObservableList<ServiceUnit> getServiceUnitData() {
        return serviceUnitDataProd;
    }

    public void init(){
        serviceUnitDataProd.clear();
        serviceUnitData = restClient.exchange(SERVER_URL + "serviceUnits",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<ServiceUnit>>(){});
        serviceUnitData.getBody().forEach(p -> {
            serviceUnitDataProd.add(p);
        });
    }
}
