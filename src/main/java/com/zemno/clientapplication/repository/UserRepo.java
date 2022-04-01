package com.zemno.clientapplication.repository;

import com.zemno.clientapplication.controller.Controller;
import com.zemno.clientapplication.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepo extends Controller {

    private ObservableList<User> userDataProd = FXCollections.observableArrayList();
    private ResponseEntity<List<User>> userData;

    public ObservableList<User> getUserDataProd() {
        return userDataProd;
    }

    public void init(){
        userDataProd.clear();
        userData = restClient.exchange(SERVER_URL + "users",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>(){});
        userData.getBody().forEach(p -> {
            userDataProd.add(p);
        });
    }
}
