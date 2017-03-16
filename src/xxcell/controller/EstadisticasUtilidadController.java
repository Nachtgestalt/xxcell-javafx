package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import xxcell.Conexion.Conexion;
import xxcell.model.ProductoVenta;

public class EstadisticasUtilidadController implements Initializable {
    
    Conexion conn = new Conexion();
    
    //Date Pickers para restringir los datos de una fecha X a una Y
    @FXML
    private JFXDatePicker datePInicio;
    @FXML
    private JFXDatePicker datePFinal;
    
    //Tabla para visualizar las ventas según las fechas
    @FXML
    private TableView<ProductoVenta> tblVentas;
    @FXML
    private TableColumn<ProductoVenta, Number> colFolio;
    @FXML
    private TableColumn<ProductoVenta, String> colFecha;
    @FXML
    private TableColumn<ProductoVenta, Number> colCantidadProd;
    @FXML
    private TableColumn<ProductoVenta, Number> colPrecioVenta;

    //Botón para inicar el llenado de la tabla y mostrar utilidad y botón para reiniciar
    @FXML
    private JFXButton btnAceptar;
    @FXML
    private JFXButton btnReiniciar;

    //Label para poner en pantalla el calculo de ventas totales de folio y la utilidad total
    @FXML
    private Label lblVenta;
    @FXML
    private Label lblUtilidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnAceptar.setOnAction((ActionEvent e) -> {       
            if(datePInicio.getValue() != null && datePFinal.getValue() != null){
                agregarDatosATablar();
            }else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al selecciónar fechas.");
                alert.setContentText("Debe selecciónar las fechas de inicio y final");
                alert.showAndWait();
            }
        }); 
    }    
    
    public void agregarDatosATablar(){
        String query = "";
    }
}
