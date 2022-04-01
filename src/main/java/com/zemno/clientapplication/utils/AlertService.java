package com.zemno.clientapplication.utils;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    public void showAlert(AlertType alertType) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(alertType.title);
        alert.setHeaderText(alertType.header);
        alert.setContentText(alertType.message);
        alert.show();
    }


    public enum AlertType {
        PASSWORD_REGEX_WARNING(
                "Недостаточно безопасный пароль",
                "Требования к паролю:",
                "Пароль должен содержать 8 символов, хотя бы одну цифру и символы верхнего и нижнего регистра\n"),
        PASSWORD_OR_LOGIN_IS_EMPTY(
                "Данные не заполнены",
                "Обратите внимание на требование",
                "Поле ввода логина или пароля не должно быть пустым."),
        USER_NOT_FOUND(
                "Ошибка при входе",
                "Повторите попытку",
                "Неверный логин или пароль."),
        PASSWORDS_NOT_MATCHES(
                "Пароли не совпадают",
                        "Повторите попытку",
                        "Пароли не совпадают."
        ),
        USER_ALREADY_EXISTS(
                "Ошибка",
                "Повторите попытку",
                "Пользователь с таким уже существует."
        ),
        SOME_FIELD_IS_EMPTY(
                "Ошибка",
                        "Повторите попытку",
                        "Заполните все необходимые поля"
        ),
        WRONG_VALUE(
                "Неверное значение",
                "Повторите попытку",
                "Введите корректное значение"
        ),
        MODEL_ALREADY_EXISTS(
                "Ошибка добавления",
                "Повторите попытку",
                "Услуга такого типа уже существует"
        ),
        MASTER_ALREADY_EXISTS(
                "Ошибка добавления",
                "Повторите попытку",
                "Такой мастер уже существует"
        ),
        SERVICE_NAME_ALREADY_EXISTS(
                "Ошибка добавления",
                "Повторите попытку",
                "Данная услуга уже существует."
        ),
        VALUE_NOT_SELECTED(
                "Выберите значение",
                "Повторите попытку",
                "Выберите значение."
        ),
        MASTER_HAS_SERVICES(
                "Ошибка удаления",
                "Вы не можете удалить мастера",
                "Мастер связан с реализуемой услугой."
        ),
        SERVICE_UNIT_WITH_THIS_NAME_THERE_IS_IN_WAREHOUSE(
                "Ошибка записи",
                "Вы не можете добавить услугу",
                "Услуга с таким именем уже существует"
        ),
        NO_ACCESS_USER(
                "Только для администратора",
                "Вы не можете перейти в этот раздел",
                "У Вас нет прав доступа."
        );

        private final String title;
        private final String message;
        private final String header;

        AlertType(String title, String header, String message) {
            this.title = title;
            this.header = header;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getTitle() {
            return title;
        }
    }
}
