package com.zemno.clientapplication.repository;

import com.zemno.clientapplication.controller.Controller;
import com.zemno.clientapplication.model.ServiceUnit;
import com.zemno.clientapplication.model.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaleRepo extends Controller {

    private ObservableList<Sale> saleDataProd = FXCollections.observableArrayList();
    private ResponseEntity<List<Sale>> saleData;
    private static ServiceUnit selectedServiceUnit;

    public static ServiceUnit getSelectedServiceUnit() {
        return selectedServiceUnit;
    }

    public static void setSelectedServiceUnit(ServiceUnit selectedServiceUnit) {
        SaleRepo.selectedServiceUnit = selectedServiceUnit;
    }

    public ObservableList<Sale> getSaleDataProd() {
        return saleDataProd;
    }

    public void init(){
        saleDataProd.clear();
        saleData = restClient.exchange(SERVER_URL + "sales", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Sale>>() {});
        saleData.getBody().forEach(sale -> {
            saleDataProd.add(sale);
        });
    }
}
