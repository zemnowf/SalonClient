package com.zemno.clientapplication.controller;

import com.zemno.clientapplication.model.*;
import com.zemno.clientapplication.repository.*;
import com.zemno.clientapplication.model.*;
import com.zemno.clientapplication.repository.*;
import com.zemno.clientapplication.utils.AlertService;
import com.zemno.clientapplication.utils.EmptyFieldValidationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FxmlView("user-page.fxml")
public class UserPageController extends Controller {

    @FXML private Label lbCaption;
    @FXML private PieChart chartPie;
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUserName;
    @FXML private TableColumn<User, String> colUserPassword;
    @FXML private TableColumn<User, String> colUserRole;
    @FXML private TextField tfUserName;
    @FXML private ComboBox<Role> cbUserRole;
    @FXML private TextField tfUserPassword;
    @FXML private TextField tfSalesSearch;
    @FXML private Label lbIncome;
    @FXML private TableView<Sale> tableSales;
    @FXML private TableColumn<Sale, String> colServiceName;
    @FXML private TableColumn<Sale, String> colType;
    @FXML private TableColumn<Sale, String> colSaleDetail;
    @FXML private TableColumn<Sale, Double> colTotalCost;
    @FXML private TableColumn<Sale, String> colDate;
    @FXML private TableColumn<Sale, String> colMasterSurname;
    @FXML
    private TextField textFieldSearch;
    @FXML
    private TableView<Master> tableMaster;
    @FXML
    private TableColumn<Master, Long> colMasterId;
    @FXML
    private TableColumn<Master, String> colMasterName;
    @FXML
    private TextField tfMasterName;
    @FXML
    private TableView<ServiceName> tableServiceName;
    @FXML
    private TableColumn<ServiceName, Long> colServiceNameId;
    @FXML
    private TableColumn<ServiceName, String> colServiceNameName;
    @FXML
    private TextField tfServiceName;
    @Autowired
    private ServiceUnitRepo serviceUnitRepo;
    @Autowired
    private ServiceNameRepo serviceNameRepo;
    @Autowired
    private MasterRepo masterRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SaleRepo saleRepo;
    private final EmptyFieldValidationService validationService;
    private final AlertService alertService;
    private User selectedUser;

    @FXML private TableView<ServiceUnit> tableServiceUnits;
    @FXML private TableColumn<ServiceUnit, String> columnName;
    @FXML private TableColumn<ServiceUnit, String> columnType;
    @FXML private TableColumn<ServiceUnit, String> columnMaster;
    @FXML private TableColumn<ServiceUnit, Double> columnServiceUnitCost;
    @FXML private TableColumn<ServiceUnit, String> columnDetail;
    @FXML
    private Pane paneAdmin;
    @FXML
    private Pane paneSales;
    @FXML
    private Pane paneMasters;
    @FXML
    private Pane paneStorage;
    @FXML
    private Pane paneChart;
    @FXML
    private StackPane paneContainer;

    public void initialize(){
        selectedUser = new User();
        paneContainer.getChildren().clear();
        paneContainer.getChildren().add(paneStorage);
        initRepo();
        FilteredList<ServiceUnit> filteredServiceUnitData = getServiceUnitFilteredList(serviceUnitRepo.getServiceUnitData(), textFieldSearch);
        tableServiceUnits.setItems(filteredServiceUnitData);
        FilteredList<Sale> filteredSaleData = getSaleFilteredList(saleRepo.getSaleDataProd(), tfSalesSearch);
//        tableSales.setItems(filteredSaleData);
        tableSales.setItems(getSortedList(filteredSaleData, tableSales));
        columnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getServiceName().getName()));
        columnType.setCellValueFactory(new PropertyValueFactory<ServiceUnit, String>("type"));
        columnMaster.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getMaster().getName()));
        columnServiceUnitCost.setCellValueFactory(new PropertyValueFactory<ServiceUnit, Double>("serviceUnitCost"));
        columnDetail.setCellValueFactory(new PropertyValueFactory<ServiceUnit, String>("detail"));
        tableMaster.setItems(masterRepo.getMasterDataProd());
        colMasterId.setCellValueFactory(new PropertyValueFactory<Master, Long>("id"));
        colMasterName.setCellValueFactory(new PropertyValueFactory<Master, String>("name"));

        tableServiceName.setItems(serviceNameRepo.getServiceNameDataProd());
        colServiceNameId.setCellValueFactory(new PropertyValueFactory<ServiceName, Long>("id"));
        colServiceNameName.setCellValueFactory(new PropertyValueFactory<ServiceName, String>("name"));

        cbUserRole.setItems(FXCollections.observableArrayList(Role.USER, Role.ADMIN));
        tableUsers.setItems(userRepo.getUserDataProd());
        colUserName.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        colUserPassword.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        colUserRole.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getRole().toString()));
        tableUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observableValue, User user, User newValue) {
                if (newValue == null)
                    newValue = selectedUser;
                selectedUser = newValue;
                System.out.println(newValue);
                tfUserName.setText(newValue.getUsername());
                tfUserPassword.setText(newValue.getPassword());
                tfUserPassword.setText(newValue.getPassword());
                cbUserRole.setValue(newValue.getRole());
            }
        });

        colServiceName.setCellValueFactory(new PropertyValueFactory<Sale, String>("serviceName"));
        colType.setCellValueFactory(new PropertyValueFactory<Sale, String>("serviceType"));
        colSaleDetail.setCellValueFactory(new PropertyValueFactory<Sale, String>("saleDetail"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<Sale, Double>("totalCost"));
        colMasterSurname.setCellValueFactory(new PropertyValueFactory<Sale, String>("masterSurname"));
        colDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDate().toString()));
        lbIncome.setText(String.format("%.2f руб.", getTotalIncome()));

        setChartPieParams();
    }

    private void initRepo() {
            serviceUnitRepo.init();
            serviceNameRepo.init();
            masterRepo.init();
            saleRepo.init();
            userRepo.init();
    }

    protected <T> SortedList<T> getSortedList(FilteredList<T> filteredData, TableView<T> table) {
        SortedList<T> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        return sortedData;
    }

    private FilteredList<ServiceUnit> getServiceUnitFilteredList(ObservableList<ServiceUnit> serviceUnits, TextField textField) {
        FilteredList<ServiceUnit> filteredData = getServiceUnitFilteredData(serviceUnits, textField);
        return filteredData;
    }

    private FilteredList<ServiceUnit> getServiceUnitFilteredData(ObservableList<ServiceUnit> serviceUnits, TextField textField) {
        FilteredList<ServiceUnit> filteredData = new FilteredList<>(serviceUnits, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(serviceUnit -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (serviceUnit.getServiceName().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (serviceUnit.getType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (serviceUnit.getDetail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (serviceUnit.getMaster().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (serviceUnit.getServiceUnitCost().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }

    private FilteredList<Sale> getSaleFilteredList(ObservableList<Sale> sales, TextField textField) {
        FilteredList<Sale> filteredData = getSaleFilteredData(sales, textField);
        return filteredData;
    }

    private FilteredList<Sale> getSaleFilteredData(ObservableList<Sale> sales, TextField textField) {
        FilteredList<Sale> filteredData = new FilteredList<>(sales, p -> true);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sale -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (sale.getServiceName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getServiceType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (sale.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getMasterName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getTotalCost().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else if (sale.getSaleDetail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        return filteredData;
    }

    public void switchToStoragePane() {
        paneContainer.getChildren().clear();
        paneContainer.getChildren().add(paneStorage);
    }

    public void switchToMastersPane() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
            paneContainer.getChildren().clear();
            paneContainer.getChildren().add(paneMasters);
        }
        else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
    }

    public void switchToSalesPane() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
            paneContainer.getChildren().clear();
            paneContainer.getChildren().add(paneSales);
        }
        else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
    }


    public void switchToChartPane() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
            paneContainer.getChildren().clear();
            paneContainer.getChildren().add(paneChart);
        }
        else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
    }

    public void switchToAdminPane() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
            paneContainer.getChildren().clear();
            paneContainer.getChildren().add(paneAdmin);
        }
        else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
    }

    public void edit() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
            if (tableServiceUnits.getSelectionModel().getSelectedItem() != null){
                serviceUnitRepo.setSelectedItemId(tableServiceUnits.getSelectionModel().getSelectedItem().getId());
                showNewStageWindow(EditServiceUnitController.class);
            } else alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
    }

    public void delete() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
            ServiceUnit selectedItem = tableServiceUnits.getSelectionModel().getSelectedItem();
            if (selectedItem == null){
                alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
            } else {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(selectedItem.getId()));
                restClient.delete(SERVER_URL + "/serviceUnit/{id}", params);
                serviceUnitRepo.init();
                tableServiceUnits.setItems(serviceUnitRepo.getServiceUnitData());
            }
        } else {
            alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
        }
    }

    public void add() {
        if (getMyUser().getRole().equals(Role.ADMIN)){
        showNewStageWindow(AddServiceUnitController.class);
    } else {
        alertService.showAlert(AlertService.AlertType.NO_ACCESS_USER);
    }
    }

    public void exit() { showCurrentStageWindow (LoginController.class, LoginController.TITLE); }

    public void update() {
        serviceUnitRepo.init();
        tableServiceUnits.setItems(getServiceUnitFilteredList(serviceUnitRepo.getServiceUnitData(), textFieldSearch));
    }

    public void addMaster() {
        String masterName = tfMasterName.getText();
        if (validationService.isEmpty(tfMasterName)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else {
            HttpEntity<Master> masterHttpEntity = new HttpEntity<>(new Master(masterName));
            Boolean result = restClient.postForObject(SERVER_URL + "/master", masterHttpEntity, Boolean.class);
            if(!result){
                alertService.showAlert(AlertService.AlertType.MASTER_ALREADY_EXISTS);
            } else {
                masterRepo.init();
                tableMaster.setItems(masterRepo.getMasterDataProd());
            }
        }
    }

    public void deleteMaster() {
        Master selectedItem = tableMaster.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(selectedItem.getId()));
            try {
                restClient.delete(SERVER_URL + "/master/{id}", params);
                masterRepo.init();
                tableMaster.setItems(masterRepo.getMasterDataProd());
            } catch (HttpServerErrorException ex){
                alertService.showAlert(AlertService.AlertType.MASTER_HAS_SERVICES);
            }
        }
    }

    public void addServiceUnitName() {
        String serviceNameName = tfServiceName.getText();
        if (validationService.isEmpty(tfServiceName)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else {
            HttpEntity<ServiceName> prNameHttpEntity = new HttpEntity<>(new ServiceName(serviceNameName));
            Boolean result = restClient.postForObject(SERVER_URL + "/serviceName", prNameHttpEntity, Boolean.class);
            if(!result){
                alertService.showAlert(AlertService.AlertType.SERVICE_NAME_ALREADY_EXISTS);
            } else {
                serviceNameRepo.init();
                tableServiceName.setItems(serviceNameRepo.getServiceNameDataProd());
            }
        }
    }

    public void deleteServiceName() {
        ServiceName selectedItem = tableServiceName.getSelectionModel().getSelectedItem();
        if (selectedItem == null){
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(selectedItem.getId()));
            try {
                restClient.delete(SERVER_URL + "/serviceName/{id}", params);
                serviceNameRepo.init();
                tableServiceName.setItems(serviceNameRepo.getServiceNameDataProd());
            } catch (HttpServerErrorException ex){
                alertService.showAlert(AlertService.AlertType.SERVICE_UNIT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE);
            }
        }
    }

    public Double getTotalIncome(){
        AtomicReference<Double> income = new AtomicReference<>(0.0);
        tableSales.getItems().forEach(sale -> {
            income.updateAndGet(v -> v + sale.getTotalCost());
        });
        return income.get();
    }

    public void sale() {
        if (tableServiceUnits.getSelectionModel().getSelectedItem() != null){
            SaleRepo.setSelectedServiceUnit(tableServiceUnits.getSelectionModel().getSelectedItem());
            showNewStageWindow(SaleServiceUnitController.class);
        } else alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
    }

    public void updateSales() {
        saleRepo.init();
        tableSales.setItems(getSaleFilteredList(saleRepo.getSaleDataProd(), tfSalesSearch));
        lbIncome.setText(String.format("%.2f рублей", getTotalIncome()));
        setChartPieParams();
    }

    public void addOrEditUser() {
        if (validationService.isEmpty(tfUserName) || validationService.isEmpty(tfUserPassword)
        || validationService.isEmpty(cbUserRole)){
            alertService.showAlert(AlertService.AlertType.SOME_FIELD_IS_EMPTY);
        } else {
            User user = new User(tfUserName.getText(), tfUserPassword.getText(), cbUserRole.getValue());
            if (selectedUser.getUsername().equals(user.getUsername())){
                user.setId(selectedUser.getId());
                restClient.put(SERVER_URL + "/user", user);
            } else {
                restClient.postForObject(SERVER_URL + "/user", user, Boolean.class);
            }
        }
    }

    public void deleteUser() {
        if (tableUsers.getSelectionModel().getSelectedItem() != null){
            User user = tableUsers.getSelectionModel().getSelectedItem();
            Map<String,String> params = new HashMap<>();
            params.put("id", String.valueOf(user.getId()));
            restClient.delete(SERVER_URL + "/user/{id}", params);
            tableUsers.getItems().remove(user);
            tableUsers.refresh();
        } else {
            alertService.showAlert(AlertService.AlertType.VALUE_NOT_SELECTED);
        }
    }

    public void updateUsers() {
        userRepo.init();
        tableUsers.setItems(userRepo.getUserDataProd());
    }

    private void setChartPieParams(){
        chartPie.getData().clear();
        saleRepo.init();
        ObservableList<Sale> sales = saleRepo.getSaleDataProd();
        Map<String, List<Sale>> map = sales.stream().collect(Collectors.groupingBy(sale -> sale.getServiceName()));
        map.forEach((key,value) ->{
            double sum = value.stream().mapToDouble(v -> v.getTotalCost()).sum();
            PieChart.Data data = new PieChart.Data(key, sum);
            chartPie.getData().add(data);
        });
        chartPie.getData().forEach(data -> data.getNode().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                lbCaption.setTranslateX(mouseEvent.getSceneX());
                lbCaption.setTranslateY(mouseEvent.getSceneY());
                lbCaption.setText(String.valueOf(data.getPieValue()) + "pie value");
            }
        }));
    }
}
