package com.zemno.clientapplication.controller;

import com.zemno.clientapplication.repository.ServiceNameRepo;
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
import org.springframework.stereotype.Component;

@Component
@FxmlView("add-service-unit-modal.fxml")
@RequiredArgsConstructor
public class AddServiceUnitController extends Controller{

    public static final String TITLE = "Новая услуга";

    @Autowired
    private MasterRepo masterRepo;
    @Autowired
    private ServiceNameRepo serviceNameRepo;
    private final EmptyFieldValidationService validationService;
    private final AlertService alertService;
    private final NumberValidationService numberValidationService;
    @FXML
    private TextField textFieldModel;
    @FXML
    private ComboBox<ServiceName> cbServiceUnitName;
    @FXML
    private TextField textFieldUnitCost;
    @FXML
    private TextField textFieldDetail;
    @FXML
    private ComboBox<Master> cbMaster;

    public void initialize(){
        masterRepo.init();
        serviceNameRepo.init();
        cbServiceUnitName.setItems(serviceNameRepo.getServiceNameDataProd());
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
        cbServiceUnitName.setConverter(new StringConverter<ServiceName>() {
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
    }

    public void add() {
        if (validationService.isEmpty(textFieldModel)
                || validationService.isEmpty(textFieldDetail)
                || validationService.isEmpty(textFieldUnitCost)
                || validationService.isEmpty(cbServiceUnitName)
                || validationService.isEmpty(cbMaster)) {
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        }
        else if (!numberValidationService.isValidDouble(textFieldUnitCost.getText())){
            alertService.showAlert(AlertService.AlertType.WRONG_VALUE);
        } else {
            HttpEntity<ServiceUnit> serviceUnitHttpEntity = new HttpEntity<>(getNewServiceUnit());
            Boolean result = restClient.postForObject(SERVER_URL + "/serviceUnit", serviceUnitHttpEntity, Boolean.class);
            if (!result){
                alertService.showAlert(AlertService.AlertType.MODEL_ALREADY_EXISTS);
            }
        }
    }

    private ServiceUnit getNewServiceUnit(){
        return new ServiceUnit(textFieldDetail.getText(),
                Double.parseDouble(textFieldUnitCost.getText()),
                textFieldModel.getText(),
                cbMaster.getValue(),
                cbServiceUnitName.getValue());
    }

}
