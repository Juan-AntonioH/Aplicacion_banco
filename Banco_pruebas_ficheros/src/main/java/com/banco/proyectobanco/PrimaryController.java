package com.banco.proyectobanco;

import com.banco.proyectobanco.Modelo.Banco;
import com.banco.proyectobanco.Modelo.CuentaBancaria;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class PrimaryController implements Initializable {

    @FXML
    private Button ingresarCuenta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
