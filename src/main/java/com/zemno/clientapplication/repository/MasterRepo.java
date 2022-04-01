package com.zemno.clientapplication.repository;

import com.zemno.clientapplication.controller.Controller;
import com.zemno.clientapplication.model.Master;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MasterRepo extends Controller {

    private ObservableList<Master> masterDataProd = FXCollections.observableArrayList();
    private List<Master> masterListProd = new ArrayList<>();
    private ResponseEntity<List<Master>> masterData;

    public ObservableList<Master> getMasterDataProd() {
        return masterDataProd;
    }

    public void init(){
        masterDataProd.clear();
        masterData = restClient.exchange(SERVER_URL + "masters",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Master>>(){});
        masterData.getBody().forEach(p -> {
            masterDataProd.add(p);
            masterListProd.add(p);
        });
    }
}
