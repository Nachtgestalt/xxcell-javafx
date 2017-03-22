package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xxcell.Conexion.Conexion;
import xxcell.model.Productos;


public class VerPromocionesController implements Initializable {

    Conexion conn = new Conexion();
    
    @FXML
    private TableView<Productos> tblPromos;

    @FXML
    private TableColumn<Productos, String> colCodigo;

    @FXML
    private TableColumn<Productos, String> colProducto;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private Label lblPrecio;
    
    String mensaje;
    Alert ErrorAlert = new Alert(Alert.AlertType.ERROR);
    Alert SuccesAlert = new Alert(Alert.AlertType.INFORMATION);

    //Lista para Llenar la tabla de productos;
    
    public ObservableList<Productos> ObtenerPromos() throws SQLException{
        ObservableList<Productos> productos = FXCollections.observableArrayList();
        String query = "SELECT * FROM promociones "
                + "INNER JOIN productos "
                + "WHERE promociones.CodigoProducto = productos.ID";
        String Nombre;
        double precio = 0;
        String codigodeComparacion;
        System.out.println(query);
        if(conn.QueryExecute(query)){
            while (conn.setResult.next())
            {
                codigodeComparacion = conn.setResult.getString("CodigoPromocion");
                if(codigodeComparacion.equals(conn.setResult.getString("CodigoPromocion"))){
                    precio = precio + conn.setResult.getDouble("PrecioPromocion");
                    Nombre = conn.setResult.getString("Marca") + " "; 
                    Nombre += conn.setResult.getString("Modelo") + " ";
                    Nombre += conn.setResult.getString("Tipo")+ " ";
                    Nombre += conn.setResult.getString("Identificador");
                    productos.add(new Productos(conn.setResult.getString("CodigoPromocion"), Nombre, precio));
                    precio = 0;
                }else
                    precio = 0;       
            }
        }
        return productos;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(Variables_Globales.Rol != "0"){
            btnEliminar.setDisable(true);
            btnEliminar.setVisible(false);
        }
        
        tblPromos.setPlaceholder(new Label("---XXCELL---"));
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colProducto.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        
        tblPromos.refresh();
        try {
            tblPromos.setItems(ObtenerPromos());
        } catch (SQLException ex) {
            Logger.getLogger(VerPromocionesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tblPromos.setOnMousePressed(new EventHandler<MouseEvent>() {
             @Override 
             public void handle(MouseEvent event) {
                if(!tblPromos.getSelectionModel().isEmpty()){
                    lblPrecio.setText(String.valueOf(tblPromos.getSelectionModel().getSelectedItem().getPrecioPub()));
                }
            }
        });
        
        btnEliminar.setOnAction((ActionEvent e) -> { 
            String query;
            if(tblPromos.getSelectionModel().isEmpty()){
                mensaje = "No se ha seleccionado ninguna promocion. \n";
                ErrorAlert.setTitle("Eliminar Promo");
                ErrorAlert.setHeaderText(null);
                ErrorAlert.setContentText(mensaje);
                ErrorAlert.initOwner(btnEliminar.getScene().getWindow());
                ErrorAlert.showAndWait();
            }
            else{
                query = "Delete from promociones where promociones.CodigoPromocion = '"+tblPromos.getSelectionModel().getSelectedItem().getID()+"'";
                System.out.println(query);
                if(conn.QueryUpdate(query))
                {
                    mensaje = "Procion Eliminada \n";
                    SuccesAlert.setTitle("Eliminar Promo");
                    SuccesAlert.setHeaderText(null);
                    SuccesAlert.setContentText(mensaje);
                    SuccesAlert.initOwner(btnEliminar.getScene().getWindow());
                    SuccesAlert.showAndWait();
                    tblPromos.refresh();
                    Stage stage;
                    stage = (Stage) btnEliminar.getScene().getWindow();
                    stage.close();
                }
                else
                {
                    ErrorAlert.setTitle("Eliminar Promo");
                    ErrorAlert.setHeaderText(null);
                    mensaje = "Promo no se ha podido Eliminar \n";
                    ErrorAlert.setContentText(mensaje);
                    ErrorAlert.initOwner(btnEliminar.getScene().getWindow());
                    ErrorAlert.showAndWait();
                }
            }
        });
    }
}
