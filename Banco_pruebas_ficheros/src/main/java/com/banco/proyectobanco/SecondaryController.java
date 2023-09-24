package com.banco.proyectobanco;

import com.banco.proyectobanco.Modelo.Banco;
import com.banco.proyectobanco.Modelo.CuentaBancaria;
import com.banco.proyectobanco.Modelo.Persona;
import com.banco.proyectobanco.Modelo.Recibo;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SecondaryController implements Initializable {

    final int MAXIMOAUTORIZADOS = 6;
    double cantidadTotalONG = 0.0;
    final int PORCENTAJEDONACION = 5;
    double porcentajeONG = PORCENTAJEDONACION / 100.0;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy.MM.dd  'Hora:  'H:mm:ss");
    String fecha = format.format(LocalDateTime.now());
    DateTimeFormatter formate = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    String hoy = formate.format(LocalDateTime.now());
    private int contadorNumeroOperacion = 1;
    private String filtroRecibos = "TODOS";
    private CuentaBancaria cuentaPersonal;
    Persona autorizadoSeleccionado;
    Banco SanJuan;
    Path fiches;
    @FXML
    private Button botonIngresar;
    @FXML
    private ChoiceBox<String> motivoIngreso;
    @FXML
    private TextArea informacionRetiro;
    @FXML
    private Button botonRetirar;
    @FXML
    private ListView<Recibo> informacionRecibos;
    private ObservableList<Recibo> listaRecibos;
    @FXML
    private ListView<Persona> informacionAutorizados;
    private ObservableList<Persona> listarAutorizados;
    @FXML
    private Button botonAutorizar;
    @FXML
    private Button botonDomiciliarRecibo;
    @FXML
    private Button botonFiltroPeriocidad;
    @FXML
    private ToggleGroup periocidades;
    @FXML
    private Button botonDesautorizar;
    @FXML
    private ProgressBar barraONG;
    @FXML
    private ProgressIndicator quesitoAutorizados;
    @FXML
    private ChoiceBox<String> motivoRetiro;
    @FXML
    private Label cantidadDonacion;
    @FXML
    private CheckBox checkDonar;
    @FXML
    private TextArea movimientos;
    @FXML
    private TabPane tabpane;
    @FXML
    private Tab textCuentas;
    @FXML
    private Button botonVerCuentas;
    @FXML
    private Button botonCrearcuenta;
    @FXML
    private Button botonSalirCuenta;
    @FXML
    private TextField textFieldNombreTitular;
    @FXML
    private TextField textFieldDniTitular;
    @FXML
    private TextField textFieldNumeroCuenta;
    @FXML
    private Spinner<Double> spinnerCantidadIngresar;
    @FXML
    private Spinner<Double> spinnerCantidadRetirar;
    @FXML
    private RadioButton radioBotonAnual;
    @FXML
    private RadioButton radioBotonTrimestral;
    @FXML
    private RadioButton radioBotonMensual;
    @FXML
    private Spinner<Double> spinnerCantidadRecibo;
    @FXML
    private TextField textFieldCifEmpresa;
    @FXML
    private TextField textFieldNombreEmpresa;
    @FXML
    private TextField textFieldConceptoRecibo;
    @FXML
    private TextField textFieldNombreAutorizado;
    @FXML
    private TextField textFieldDniAutorizado;
    @FXML
    private Label saldoCuenta;
    @FXML
    private Label titularCuenta;
    @FXML
    private Tab pestañaIngresar;
    @FXML
    private Tab pestañaRetirar;
    @FXML
    private Tab pestañaAutorizados;
    @FXML
    private Tab pestañaMovimientos;
    @FXML
    private Tab pestañaDomiciliar;
    @FXML
    private TextArea informacionIngreso;
    @FXML
    private Button botonAccederCuenta;
    @FXML
    private Text textoInformativoCuenta;
    @FXML
    private Label labelNumeroDeAutorizados;
    @FXML
    private Button botonImportar;
    @FXML
    private Button botonExportarRecibos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Crea un objeto inicial del banco
        SanJuan = new Banco();
        //Crea una cuenta predeterminada con la que poder trabajar namas iniciar la aplicación
        crearCuenta();
        //Carga los datos de la cuenta y funciones
        iniciarDatos();
        motivoIngreso();
        motivoRetirada();
        metodosSpinner();
    }

    //Pestaña Ingresar
    @FXML
    private void alPulsarIngreso(ActionEvent event) {
        //Comprueba que el valor no es nulo o es un 0
        try {
            if (spinnerCantidadIngresar.getValue() == null || spinnerCantidadIngresar.getValue() <= 0) {
                mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "No ha introducido una cantidad!", "Debe introducir una cantidad para realizar el ingreso");
            } else {
                ingresoInfo(spinnerCantidadIngresar.getValue());
            }
        } catch (Exception e) {
            mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "No ha introducido una cantidad!", "Debe introducir una cantidad para realizar el ingreso");
        }

    }

    public void ingresoInfo(double cantidadSpinner) { // metodo que muestra la informacion de la opcion seleccionada en el ingreso
        final int CANTIDADTOPHACIENDA = 3000; //Limite para avisar a hacienda.
        String mensajeVentana = "--------------OPERACIÓN Nº "
                + contadorNumeroOperacion + ": INGRESO--------------" + "\nCantidad ingresada: "
                + cantidadSpinner + " Euros." + "\nMotivo del ingreso: "
                + motivoIngreso.getValue() + "."
                + "\nFecha del ingreso: " + fecha
                + "\nSu saldo ha sido actualizado."
                + "\nMuchas gracias por usar nuestra aplicación.\n";
        informacionIngreso.setText(mensajeVentana);
        movimientos.appendText(mensajeVentana + "\n");
        contadorNumeroOperacion++;
        if (cantidadSpinner >= CANTIDADTOPHACIENDA) { // Si la cantidad ingresada es igual o mayor a 3000 aviso a hacienda.
            mensaje(Alert.AlertType.WARNING, "Banco SanJuan", "AVISO: NOTIFICAR A HACIENDA", "El motivo es haber ingresado " + cantidadSpinner + "€");
            informacionIngreso.appendText("NOTIFICAR A HACIENDA. \nLa cantidad ingresada supera los 3000 €." + "\nMuchas gracias por usar nuestra aplicación.");
        }
        incrementarSaldo(cantidadSpinner);
    }

    public void incrementarSaldo(double cantidadSpinner) {        //Actualizar el saldo

        cuentaPersonal.ingresar(cantidadSpinner);
        saldoCuenta.setText(cuentaPersonal.getSaldoFormateado());
        informacionCuenta();
    }

    public void motivoIngreso() { //Opciones con el motivo por el que ingresa esa cantidad.
        //Creamos las opciones de ingreso
        motivoIngreso.getItems().add("Nómina");
        motivoIngreso.getItems().add("Regalo");
        motivoIngreso.getItems().add("Donación");
        motivoIngreso.getItems().add("Otros");
    }

//Pestaña Retirar
    @FXML
    private void alPulsarRetiro(MouseEvent event) {
        try {
            if (spinnerCantidadRetirar.getValue() >= 0 || spinnerCantidadRetirar.getValue() != null) { // Comprueba que la cantidad sea mayor a 0 y no nula.
                retiradaInfo(spinnerCantidadRetirar.getValue());
            } else {
                mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "No ha introducido una cantidad!", "Debe introducir una cantidad para realizar la retirada");
            }
        } catch (Exception e) {
            mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "No ha introducido una cantidad!", "Debe introducir una cantidad para realizar la retirada");
        }

    }

    public void retiradaInfo(double cantidadSpinner) { // Método que muestra la informacion de la opcion seleccionada en la retirada
        String mensajeDonacion = ""; // Cadena que se rellena según ocurre cosas en el método
        double cantidadExtraONG = 0.0; // Cantidad para ONG si activa el checkbox
        final double SALDOTOP = -50.0; // Mínimo saldo posible en la cuenta
        final int CERO = 0; // El número 0
        String motivo = this.motivoRetiro.getValue();
        double cantidadRestante = cuentaPersonal.getSaldo() - cantidadSpinner; //Saldo - Cantidad retirada
        if (checkDonar.isSelected()) {// Comprueba si el checkbox está seleccionado.
            cantidadExtraONG = cantidadSpinner * porcentajeONG;
            cantidadRestante -= cantidadExtraONG;
            String contenidoOng = String.format("%.2f", cantidadExtraONG) + "€";
            mensajeDonacion = "\nGracias por la donación de: " + contenidoOng;
        }
        if (cantidadRestante >= SALDOTOP) { // Comprueba que la cantidad a retirar no supere el limite permitido por la cuenta.
            String mensajeVentana = "----------OPERACIÓN Nº "
                    + contadorNumeroOperacion + ": RETIRADA-------------" + "\nCantidad retirada: "
                    + "-" + cantidadSpinner + " Euros." + "\nMotivo de la retirada: "
                    + motivo + "."
                    + "\nFecha de la retirada: " + fecha
                    + "\nSu saldo ha sido actualizado."
                    + mensajeDonacion
                    + "\nMuchas gracias por usar nuestra aplicación.\n";
            informacionRetiro.setText(mensajeVentana);
            movimientos.appendText(mensajeVentana + "\n");
            if (cantidadRestante >= SALDOTOP && cantidadRestante < CERO) { // Si saldo es negativo avisa con un mensaje
                mensaje(Alert.AlertType.WARNING, "Banco SanJuan", "AVISO: SALDO NEGATIVO", "Será mejor que ingreses algo pronto");
            }
            reducirSaldo(cantidadExtraONG, cantidadSpinner);
            contadorNumeroOperacion++;
        } else {
            mensaje(Alert.AlertType.WARNING, "Banco SanJuan", "AVISO: MÍNIMO SALDO ES DE -50€", "Necesitas ganar dinero, hippie");
            informacionRetiro.setText("Has entrado en números rojos." + "\nMuchas gracias por usar nuestra aplicación.");
        }
    }

    public void reducirSaldo(double ong, double cantidadSpinner) {
        //Actualizar el saldo
        String mensajeONG = "";
        final int TOPEONG = 100;
        cantidadTotalONG = cantidadTotalONG + ong;
        double valor = cantidadTotalONG / TOPEONG;
        //Carga de la barra y mostrar cantidad al lado
        barraONG.setProgress(valor);

        if (cantidadTotalONG > TOPEONG) {// Comprueba si la cantidad excede el límite permitido.
            // Deshabilita las donaciones al llegar al límite
            checkDonar.setDisable(true);
            checkDonar.setSelected(false);
            // Comprueba si la donación es mayor al permitido.
            double resto = cantidadTotalONG - TOPEONG; // Extrae de la donación lo que exceda los 100€
            cantidadTotalONG = TOPEONG;
            ong = 0;
            mensajeONG = "Alcanzastes el máximo permitido de donación a las ONGs.\n¡MUCHAS GRACIAS!\n"
                    + "Se ingreso la cantidad que sobrepasó los 100 euros de donación: "
                    + String.format("%.2f", resto) + "€";
            incrementarSaldo(resto);
            informacionRetiro.appendText(mensajeONG);
            movimientos.appendText(mensajeONG);
        }

        String contenido = String.format("%.2f", cantidadTotalONG) + "€";
        cantidadDonacion.setText(contenido);
        cuentaPersonal.sacar(cantidadSpinner + ong);
        informacionCuenta();
    }

    public void motivoRetirada() {
        //Creamos las opciones de retiro
        motivoRetiro.getItems().add("Nómina");
        motivoRetiro.getItems().add("Regalo");
        motivoRetiro.getItems().add("Donación");
        motivoRetiro.getItems().add("Otros");
    }

    //Pestaña Domiciliar
    @FXML
    private void alPulsarDomiciliarRecibo(MouseEvent event) {
        // Recoge los datos introducido por cada parámetro.
        Double cantidadSpinner = spinnerCantidadRecibo.getValue();
        String cifEmpresa = textFieldCifEmpresa.getText();
        String nombreEmpresa = textFieldNombreEmpresa.getText();
        String conceptoRecibo = textFieldConceptoRecibo.getText();
        String periocidad;
        // Establece como parámetro la opción seleccionada del radio de botones.
        if (radioBotonAnual.isSelected()) {
            periocidad = "ANUAL";
        } else if (radioBotonTrimestral.isSelected()) {
            periocidad = "TRIMESTRAL";
        } else if (radioBotonMensual.isSelected()) {
            periocidad = "MENSUAL";
        } else {
            periocidad = "";
        }
        // Llama al método domicilar según parámetros y recoge la información de retorno.
        String mensaje = cuentaPersonal.domiciliar(cifEmpresa, nombreEmpresa, cantidadSpinner, conceptoRecibo, periocidad);
        // Si no se pudo domiciliar el recibo muestra un Warning con los posibles errores.
        if (!mensaje.contains("El recibo se ha creado correctamente")) {
            mensaje(Alert.AlertType.WARNING, "Banco SanJuan", "Error al domiciliar el recibo, los siguientes motivos son:",
                    mensaje);
        }
        //Restablece la lista de recibos.
        filtroRecibos = "TODOS";
        listarecibos();
    }

    @FXML
    private void alPulsarFiltraPeriocidad(MouseEvent event) {
        //Llama al método para mostrar una ventana con la información pasada por parámetro.
        elegirOpcionDomicilar("Banco SanJuan", "¿Por que periocidad quieres filtrar?", "Elige una opción");
    }

    private void listarecibos() {
        //Muestra la lista recibos según filtro, por defecto muestra todos los recibos.
        if (filtroRecibos.equalsIgnoreCase("TODOS")) { //Muestra la lista de todos los recibos
            listaRecibos = FXCollections.observableArrayList(cuentaPersonal.getListaRecibos());
        } else { // Muestra la listra por el filtro seleccionado.
            listaRecibos = FXCollections.observableArrayList(cuentaPersonal.listaRecibosDomicialidos(filtroRecibos));
        }
        informacionRecibos.setItems(listaRecibos);
    }

    private void elegirOpcionDomicilar(String titulo, String cabecera, String texto) {
        //Muestra una ventana con un mensaje y unas opciones para preestablecidas para filtrar los recibos. 
        List<String> elegirPeriocidad = new ArrayList<>(Arrays.asList("ANUAL", "TRIMESTRAL", "MENSUAL", "TODOS"));
        ChoiceDialog dialogo = new ChoiceDialog(filtroRecibos, elegirPeriocidad);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(cabecera);
        dialogo.setContentText(texto);
        Optional<String> resultado = dialogo.showAndWait();
        if (resultado.isPresent()) {//Cambia el filtro de recibos a mostrar según opcion escogida.
            filtroRecibos = resultado.get();
        }
        // Método que se encarga de mostrar la lista según parámetros.
        listarecibos();
    }

    // Pestaña autorizados
    @FXML
    private void alPulsarAutorizarPersonas(MouseEvent event) {
        boolean verificar = true; // variable para asegurar que hace o que no hace según los parametros introducidos.
        String mensajeError = ""; // Guarda todo lo que sucede y muestra al final según las posibles variables.
        final String PATRON = "^\\d{8}[a-zA-Z]"; // condicion para introducir un DNI, 8 números y una letra.
        // Recoge los valores de nombre y DNI introducidos.
        String nombre = textFieldNombreAutorizado.getText();
        String DNI = textFieldDniAutorizado.getText().toUpperCase();
        // Lista de comprobacines, si se cumple las condiciones y si los datos son nulos o solo espacios.
        if (nombre.isBlank()) {// Comprobar que no esta vacio o solo espacio.
            mensajeError += "Debes introducir un nombre correcto\n";
            verificar = false;
        }
        if (!DNI.matches(PATRON)) {// Comprobar que cumple el patrón solicitado.
            mensajeError += "Has introducido un DNI incorrecto\n";
            verificar = false;
        }
        if (verificar) {// Comprueba si ya hay alguien en la lista de autorizados con ese DNI.
            for (Persona autorizados : listarAutorizados) {
                if (autorizados.igual(DNI)) {
                    verificar = false;
                    mensajeError = "Ya tienes autorizado a una persona con el DNI:\n" + DNI;
                    break;
                }
            }
            if (verificar) {// Informa de que no puedes autorizar mas personas si supera el limite.
                if (listarAutorizados.size() >= MAXIMOAUTORIZADOS) {
                    mensajeError = "Solo puedes tener un maximo de 6 personas\ncon autorización en tú cuenta bancaria.";
                    verificar = false;
                } else {
                    Persona autorizar = new Persona(DNI, nombre);
                    cuentaPersonal.autorizar(autorizar);
                }
            }
        }
        if (!verificar) {// Si falla alguna condicion de las anteriores, se muestra una ventana Error con los datos recogidos.
            mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "Error al autorizar", mensajeError);
        }
        //Actualiza lista y parámetros.
        listarAutorizados();
        textFieldNombreAutorizado.setText("");
        textFieldDniAutorizado.setText("");
    }

    private void listarAutorizados() {
        // Recoge la lista de autorizados de la cuenta.
        listarAutorizados = FXCollections.observableArrayList(cuentaPersonal.getAutorizados());
        // Añade al ListView esa lista.
        informacionAutorizados.setItems(listarAutorizados);
        // Divide el número de personas por el total posible para mostrar el %.
        Double valor = (double) listarAutorizados.size() / MAXIMOAUTORIZADOS;
        quesitoAutorizados.setProgress(valor);
        labelNumeroDeAutorizados.setText("" + listarAutorizados.size());
        if (listarAutorizados.size() == MAXIMOAUTORIZADOS) { // Habilita o deshabilita el boton autorizar dependiendo la cantidad.
            botonAutorizar.setDisable(true);
        } else {
            botonAutorizar.setDisable(false);
        }
    }

    @FXML
    private void seleccionarAutorizado(MouseEvent event) {
        // Coge los datos de la persona según el seleccionado.
        try {
            int posicionAutorizado = informacionAutorizados.getSelectionModel().getSelectedIndex();
            if (posicionAutorizado >= 0) {// Evita error si clickeas en la ventana sin seleccionar ninguna persona la primera vez. El error no sucede si clickeas despues de seleccionar a alguien.
                autorizadoSeleccionado = listarAutorizados.get(posicionAutorizado);
                textFieldDniAutorizado.setText(autorizadoSeleccionado.getNif());
                textFieldNombreAutorizado.setText(autorizadoSeleccionado.getNombre());
            }
        } catch (Exception e) { // Por si ocurre algún otro tipo de error.
            e.getMessage();
        }
    }

    @FXML
    private void alPulsarDesautorizarPersonas(MouseEvent event) {
        Persona autorizado = autorizadoSeleccionado;
        if (autorizado != null) { // Mensaje si no hay nadie seleccionado.
            mensaje(Alert.AlertType.CONFIRMATION, "Banco SanJuan", "Estas apunto de desautorizar a: " + autorizado.toString(), "¿Estás seguro de borrarlo?\n");
        } else { // Muestra una ventana pregunando si esta seguro de desautorizar.
            mensaje(Alert.AlertType.WARNING, "Banco SanJuan", "", "Para desautorizar a alguien primero seleccionalo de la lista.");
        }
    }

    private void ventanaConfirmacionDesautorizar(Alert mensaje) {
        //Ventana con botones para confirmar el desautorizar a la persona
        ButtonType botonSI = new ButtonType("SI");
        ButtonType botonNO = new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
        mensaje.getButtonTypes().setAll(botonSI, botonNO);
        Optional<ButtonType> resultado = mensaje.showAndWait(); // Guardar en una variable el mensaje
        String eleccion = resultado.get().getText(); // Recoger el texto de la opción clicada
        if (eleccion.equals("SI")) {// Desautorizar la persona si clickea en SI
            cuentaPersonal.desautorizar(autorizadoSeleccionado);
            listarAutorizados();
            // Limpiar selección y label con DNI de esa persona
            autorizadoSeleccionado = null;
            textFieldDniAutorizado.setText("");
            textFieldNombreAutorizado.setText("");
        }
    }

    // Pestaña Cuentas
    @FXML
    private void alPulsarVerCuentas(MouseEvent event) {
        //Muestra en una ventana de diálogo las cuentas actuales.
        String cuentasGuardadas = "";
        for (long numeroCuenta : SanJuan.listaNumcuenta()) {//Recoge cada cuenta y extrae el titular y el número
            CuentaBancaria cuenta = SanJuan.localizaCC(numeroCuenta);
            cuentasGuardadas += "Titular: " + cuenta.getTitular().getNombre() + "   -   Nº Cuenta: "
                    + numeroCuenta + "\n";
        }
        mensaje(Alert.AlertType.INFORMATION, "Banco SanJuan", "Las cuentas guardadas en nuestro banco son las siguientes",
                cuentasGuardadas);
    }

    @FXML
    private void alPulsarCrearCuenta(MouseEvent event) {
        // Mínimo y máximo del número aleatorio
        final int MINALEATORIO = 100000000;
        final int MAXALEATORIO = 999999999;
        boolean verificar = true;
        String nombre = textFieldNombreTitular.getText();
        String DNI = textFieldDniTitular.getText();
        final String PATRON = "^\\d{8}[a-zA-Z]"; // condicion para introducir un DNI, 8 números y una letra.
        String mensajeError = "";
        if (nombre.isBlank()) {// Comprobar que no esta vacio o solo espacio.
            mensajeError += "Debes introducir un nombre correcto\n";
            verificar = false;
        }
        if (!DNI.matches(PATRON)) {// Comprobar que cumple el patrón solicitado.
            mensajeError += "Has introducido un DNI incorrecto\n";
            verificar = false;
        }
        if (verificar) {//Si todo lo anterior
            int NumeroCuenta = (int) (Math.random() * (MAXALEATORIO - MINALEATORIO + 1));
            if (SanJuan.localizaCC(NumeroCuenta) == null) {// Si no existe cuenta con ese número se crea.
                Persona titularCuenta = new Persona(DNI, nombre);
                CuentaBancaria cuenta = new CuentaBancaria(NumeroCuenta, titularCuenta);
                SanJuan.almacenarCC(cuenta);
                mensaje(Alert.AlertType.INFORMATION, "Banco SanJuan", "La cuenta fue creada correctamenta con la siguiente información.",
                        "Titular: " + titularCuenta.getNombre() + "\nDNI: " + titularCuenta.getNif() + "\nNº cuenta: " + NumeroCuenta);
                establecerPorDefectoPestañas();
            } else {
                mensajeError += "Ese número de cuenta ya existe\n";
            }
        } else {
            mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "Error al crear la cuenta", mensajeError);
        }
    }

    @FXML
    private void alPulsarIngresarCuenta(MouseEvent event) {
        boolean verificar = true;
        String mensajeError = "";
        final String PATRON = "^[1-9]\\d{8}";
        String numeroIntroducido = textFieldNumeroCuenta.getText();
        if (!numeroIntroducido.matches(PATRON)) {//Comprueba que el número introducido cumple los parámetros
            verificar = false;
            mensajeError = "Has introducido un número de cuenta incorrecto\nDebes introducir un número de 8 dígitos";
        }
        if (verificar) {
            long numeroCuenta = Long.parseLong(numeroIntroducido);
            if (SanJuan.localizaCC(numeroCuenta) != null) { // Accedes a la cuenta si existe.
                cuentaPersonal = SanJuan.localizaCC(numeroCuenta);
                iniciarDatos();
                establecerPorDefectoPestañas();
                donacionesOng();
            } else { // si no existe la cuenta muestra un mensaje.
                mensajeError = "El número de cuenta: " + numeroIntroducido + " no existe en nuestro banco.";
            }
        }
        if (!verificar) { // Envia la información al metodo mensaje, con los posibles errores.
            mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "Hubo un problema al iniciar sesión con la cuenta", mensajeError);
        }
    }

    @FXML
    private void alPulsarSalir(MouseEvent event) {
        //Sales de la cuenta y deshabilitas las otras pestañas
        cuentaPersonal = null;
        deshabilitarPestañas();
        titularCuenta.setText("");
        saldoCuenta.setText("");
    }

    //Miscelanea
    private void mensaje(Alert.AlertType tipo, String titulo, String cabecera, String texto) {
        // Muestra la ventana según tipo y contenido que le pasen por parámetro.
        Alert mensaje = new Alert(tipo);
        mensaje.setTitle(titulo);
        mensaje.setHeaderText(cabecera);
        mensaje.setContentText(texto);
        // Si es un mensaje de confirmación, redirige a otro método para gestionar los botones.
        if (Alert.AlertType.CONFIRMATION == tipo) {
            ventanaConfirmacionDesautorizar(mensaje);
        } else {
            mensaje.showAndWait();
        }
    }

    private void crearCuenta() { // Crea una cuenta inicial con autorizados y recibos
        // Creación de las personas
        Persona juan = new Persona("33333333C", "Juan Antonio");
        Persona autorizado1 = new Persona("44444444D", "Esteban Adarme");
        Persona autorizado2 = new Persona("55555555E", "Daniel Cano");
        Persona autorizado3 = new Persona("66666666F", "Alvaro Garcia");
        // Creación de la cuenta bancaria
        cuentaPersonal = new CuentaBancaria(334567891, juan);
        SanJuan.almacenarCC(cuentaPersonal);
        // Autorizar a otras personas a la cuenta
        cuentaPersonal.autorizar(autorizado2);
        cuentaPersonal.autorizar(autorizado1);
        cuentaPersonal.autorizar(autorizado3);
        // Domiciliar recibos a la cuenta
        cuentaPersonal.domiciliar("2000000A", "EMIVASA", 40.00, "Agua", "TRIMESTRAL");
        cuentaPersonal.domiciliar("3000000A", "Telefonica", 60.00, "Teléfono", "ANUAL");
        cuentaPersonal.domiciliar("4000000A", "Línea directa", 250.00, "Seguro coche", "MENSUAL");
        cuentaPersonal.domiciliar("4000000B", "Línea directa", 250.00, "Seguro coche", "MENSUAL");
        // Ingreso de prueba
        cuentaPersonal.ingresar(100);
        // Retiro de prueba
//        cuentaPersonal.sacar(123890);
    }

    private void informacionCuenta() { // Muestra el titular y el saldo de la cuenta"color depende de la cantidad"
        // Mostar titular y saldo de la cuenta
        String titular = cuentaPersonal.getTitular().getNombre();
        saldoCuenta.setText(cuentaPersonal.getSaldoFormateado());
        String contenido = String.format("%.2f", cantidadTotalONG) + "€";
        cantidadDonacion.setText(contenido);
        double saldo = cuentaPersonal.getSaldo();
        titularCuenta.setText(titular);
        // Comprobar saldo para establecer un color según cantidad
        if (saldo < 0) {
            saldoCuenta.setStyle("-fx-text-fill: red");
        } else if (saldo == 0) {
            saldoCuenta.setStyle("-fx-text-fill: black");
        } else {
            saldoCuenta.setStyle("-fx-text-fill: green");
        }
    }

    private void metodosSpinner() { // Especifica el funcionamiento del spinner
        //retirar
        SpinnerValueFactory.DoubleSpinnerValueFactory retirar = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 30000, 100, 5);
        spinnerCantidadRetirar.setValueFactory(retirar);
        //recibo
        SpinnerValueFactory.DoubleSpinnerValueFactory importe = new SpinnerValueFactory.DoubleSpinnerValueFactory(5, 55000, 100, 5);
        spinnerCantidadRecibo.setValueFactory(importe);
        //ingresar
        SpinnerValueFactory.DoubleSpinnerValueFactory ingresar = new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 25000, 100, 5);
        spinnerCantidadIngresar.setValueFactory(ingresar);

    }

    private void iniciarDatos() { // Carga todos los datos de la cuenta con la que accedes
        establecerPorDefectoPestañas();
        informacionCuenta();
        listarecibos();
        listarAutorizados();
        habilitarPestañas();

    }

    private void deshabilitarPestañas() { // Deshabilita las pestañas al pulsar el botón salir
        // Deshabilita pestañas
        pestañaAutorizados.setDisable(true);
        pestañaDomiciliar.setDisable(true);
        pestañaIngresar.setDisable(true);
        pestañaMovimientos.setDisable(true);
        pestañaRetirar.setDisable(true);
        // Habilita el botón acceder cuenta y el textfield
        botonAccederCuenta.setDisable(false);
        textFieldNumeroCuenta.setDisable(false);
        // Desaparece el texto informativo para acceder a otra cuenta
        textoInformativoCuenta.setVisible(false);
    }

    private void habilitarPestañas() { // Habilita las pestañas tras acceder con una cuenta
        // Habilita pestañas
        pestañaAutorizados.setDisable(false);
        pestañaDomiciliar.setDisable(false);
        pestañaIngresar.setDisable(false);
        pestañaMovimientos.setDisable(false);
        pestañaRetirar.setDisable(false);
        // Deshabilita el botón de acceder cuenta y el textfield
        botonAccederCuenta.setDisable(true);
        textFieldNumeroCuenta.setDisable(true);
        // Aparece el texto informativo para acceder a otra cuenta
        textoInformativoCuenta.setVisible(true);
    }

    private void establecerPorDefectoPestañas() { //Limpia las pestañas dejándolas como cuando inicias la aplicación
        textFieldCifEmpresa.setText("");
        textFieldConceptoRecibo.setText("");
        textFieldNombreAutorizado.setText("");
        textFieldDniAutorizado.setText("");
        textFieldDniTitular.setText("");
        textFieldNombreEmpresa.setText("");
        textFieldNombreTitular.setText("");
        textFieldNumeroCuenta.setText("");
        metodosSpinner();
        informacionRetiro.setText("");
        informacionIngreso.setText("");
        movimientos.setText("");
        filtroRecibos = "TODOS";
    }

    private void donacionesOng() {
        checkDonar.setDisable(false);
        checkDonar.setSelected(false);
        cantidadTotalONG = 0;
        barraONG.setProgress(0);
    }

    @FXML
    private void alPulsarImportar(MouseEvent event) throws IOException {
        int contador = 0;
        String mensajeError = "";
        Path archivo = Paths.get("autorizados.txt");
        try ( Stream<String> contenidoArchivo = Files.lines(archivo, Charset.defaultCharset())) {
            Iterator<String> it = contenidoArchivo.iterator();
            while (it.hasNext()) {
                String linea = it.next();
                String[] autorizados = linea.split("#");
                if (listarAutorizados.size() >= MAXIMOAUTORIZADOS) {
                    mensajeError += "No se puede autorizar a la persona: " + autorizados[0] + " - " + autorizados[1] + "\n";
                    contador++;
                } else {
                    Persona autorizar = new Persona(autorizados[1], autorizados[0]);
                    cuentaPersonal.autorizar(autorizar);
                }
                listarAutorizados();
            }
        } catch (IOException ex) {
            System.out.println("Error en la lectura del archivo");
        }
        if (contador > 0) {// Si falla alguna condicion de las anteriores, se muestra una ventana Error con los datos recogidos.
            mensaje(Alert.AlertType.ERROR, "Banco SanJuan", "Error al autorizar", mensajeError + "\n MASA PEÑA");
        }
    }

    @FXML
    private void alPulsarExportarRecibos(MouseEvent event) throws IOException {
        int sumaTotal = 0;
        //Borrar todos los ficheros por periocidad--------------------------------
//        Path directorio = Paths.get("");
//        Stream<Path> datos = Files.list(directorio);
//        Iterator<Path> it = datos.iterator();
//        while (it.hasNext()) {
//            Path ficheros = it.next();
//            String fiche =ficheros.toFile().getName();
//            if (fiche.contains("MENSUAL")|fiche.contains("TRIMESTRAL")|fiche.contains("ANUAL")|fiche.contains("TODOS")) {
//                ficheros.toFile().delete();
//            }
//        }
        //---------------------------------------------
        //Mantener solo un fichero--------------
        if (fiches != null) {
            fiches.toFile().delete();
        }
        //--------------------
        fiches = Paths.get(cuentaPersonal.getTitular().getNombre() + "-" + filtroRecibos + hoy + ".txt");
        try ( BufferedWriter out = Files.newBufferedWriter(fiches, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
            out.write("Titular: " + cuentaPersonal.getTitular().getNombre() + " - Nº Cuenta: "
                    + cuentaPersonal.getNumCuenta());
            out.newLine();
            for (Recibo recibo : listaRecibos) {
                out.append(recibo.toString());
                out.newLine();
                sumaTotal += recibo.getImporte();
            }
            out.append("La suma total de los recibos es: " + sumaTotal);
        } catch (IOException e) {
            System.out.println("Error al escribir");
        }
    }
}
