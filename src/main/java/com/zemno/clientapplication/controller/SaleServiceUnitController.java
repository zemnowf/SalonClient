package com.zemno.clientapplication.controller;

import com.zemno.clientapplication.repository.SaleRepo;
import com.zemno.clientapplication.utils.AlertService;
import com.zemno.clientapplication.utils.NumberValidationService;
import com.zemno.clientapplication.model.ServiceUnit;
import com.zemno.clientapplication.model.Sale;
import com.zemno.clientapplication.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@FxmlView("sale-service-unit-modal.fxml")
@RequiredArgsConstructor
public class SaleServiceUnitController extends Controller{

    public static final String TITLE = "Реализация услуги";

    @FXML
    private TextField tfAmountForSale;
    private final AlertService alertService;
    private final NumberValidationService numberValidationService;


    public void signDeal() {
        ServiceUnit serviceUnitForSale = SaleRepo.getSelectedServiceUnit();
        User user = getMyUser();
        Sale sale = new Sale(serviceUnitForSale.getServiceName().getName(),
                        serviceUnitForSale.getType(),
                        serviceUnitForSale.getDetail(), serviceUnitForSale.getServiceUnitCost(),
                        serviceUnitForSale.getMaster().getName());
        restClient.postForObject(SERVER_URL + "/sale", new HttpEntity<>(sale), Sale.class);
        serviceUnitForSale.setDetail(serviceUnitForSale.getDetail());
        restClient.exchange(SERVER_URL + "/serviceUnit/update", HttpMethod.PUT, new HttpEntity<>(serviceUnitForSale), Boolean.class);
    }
}
