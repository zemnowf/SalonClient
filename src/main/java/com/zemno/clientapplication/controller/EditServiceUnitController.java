package com.zemno.clientapplication.controller;

import com.zemno.clientapplication.repository.ServiceNameRepo;
import com.zemno.clientapplication.repository.ServiceUnitRepo;
import com.zemno.clientapplication.repository.MasterRepo;
import com.zemno.clientapplication.utils.AlertService;
import com.zemno.clientapplication.utils.EmptyFieldValidationService;
import com.zemno.clientapplication.utils.NumberValidationService;
import com.zemno.clientapplication.model.ServiceUnit;
import com.zemno.clientapplication.model.ServiceName;
import com.zemno.clientapplication.model.Master;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@FxmlView("edit-service-unit-modal.fxml")
@RequiredArgsConstructor
public class EditServiceUnitController extends Controller{
    public static final String TITLE = "Редактирование услуги";
    @Autowired
    private MasterRepo masterRepo;
    @Autowired
    private ServiceNameRepo serviceNameRepo;
    @Autowired
    ServiceUnitRepo serviceUnitRepo;
    private final EmptyFieldValidationService validationService;
    private final AlertService alertService;
    private final NumberValidationService numberValidationService;
    @FXML
    private TextField textFieldModel;
    @FXML
    private ComboBox<ServiceName> cbServiceName;
    @FXML
    private TextField textFieldUnitCost;
    @FXML
    private TextField textFieldDetail;
    @FXML
    private ComboBox<Master> cbMaster;

    public void initialize(){
        masterRepo.init();
        serviceNameRepo.init();
        cbServiceName.setItems(serviceNameRepo.getServiceNameDataProd());
        cbMaster.setItems(masterRepo.getMasterDataProd());
        cbMaster.setConverter(new StringConverter<Master>() {
            @Override
            public String toString(Master master) {
                if (master != null)
                    return master.getName();
                return "";
            }

            @Override
            public Master fromString(String s) {
                return null;
            }
        });
        cbServiceName.setConverter(new StringConverter<ServiceName>() {
            @Override
            public String toString(ServiceName serviceName) {
                if (serviceName != null)
                    return serviceName.getName();
                return "";
            }

            @Override
            public ServiceName fromString(String s) {
                return null;
            }
        });
        fillFieldsById(serviceUnitRepo.getSelectedItemId());
    }


    private ServiceUnit getNewServiceUnit(){
        return new ServiceUnit(textFieldDetail.getText(),
                Double.parseDouble(textFieldUnitCost.getText()),
                textFieldModel.getText(),
                cbMaster.getValue(),
                cbServiceName.getValue());
    }

    private void fillFieldsById(Long id){
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(serviceUnitRepo.getSelectedItemId()));
        ServiceUnit serviceUnit = restClient.getForObject(SERVER_URL + "/serviceUnit/{id}", ServiceUnit.class, params);
        System.out.println(serviceUnit);
        textFieldDetail.setText(serviceUnit.getDetail());
        textFieldUnitCost.setText(String.valueOf(serviceUnit.getServiceUnitCost()));
        textFieldModel.setText(serviceUnit.getType());
        cbServiceName.setValue(serviceUnit.getServiceName());
        cbMaster.setValue(serviceUnit.getMaster());
    }

    public void edit() {
        if (validationService.isEmpty(textFieldModel)
                || validationService.isEmpty(textFieldDetail)
                || validationService.isEmpty(textFieldUnitCost)
                || validationService.isEmpty(cbServiceName)
                || validationService.isEmpty(cbMaster)) {
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        }
        else if (!numberValidationService.isValidDouble(textFieldUnitCost.getText())){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else {
            ServiceUnit serviceUnit = getNewServiceUnit();
            serviceUnit.setId(serviceUnitRepo.getSelectedItemId());
//            restClient.put(SERVER_URL + "/serviceUnit/update/{id}", getNewServiceUnit(), params);
            ResponseEntity<Boolean> result = restClient.exchange(SERVER_URL + "/serviceUnit/update", HttpMethod.PUT, new HttpEntity<>(serviceUnit), Boolean.class);
            if (!result.getBody().booleanValue()){
                alertService.showAlert(AlertService.AlertType.MODEL_ALREADY_EXISTS);
            }
        }
    }
}
