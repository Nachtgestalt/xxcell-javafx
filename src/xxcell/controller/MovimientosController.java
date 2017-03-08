/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static xxcell.controller.LoginController.scene;

/**
 * FXML Controller class
 *
 * @author Adrián Pérez
 */
public class MovimientosController implements Initializable {

    @FXML
    private JFXButton btnPrestamos;

    @FXML
    private JFXButton btnSalidas;
    
     @FXML
    private JFXButton btnMostrarPrestamos;

    @FXML
    private JFXButton btnAgregarPromos;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnPrestamos.setOnAction((ActionEvent e) -> {       
            Parent principal;
            try {
                principal = FXMLLoader.load(getClass().getResource("/xxcell/view/Transacciones.fxml"));
                Stage principalStage = new Stage();
                principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
                scene = new Scene(principal);
                principalStage.setScene(scene);
                principalStage.initModality(Modality.APPLICATION_MODAL);
                principalStage.initOwner(btnPrestamos.getScene().getWindow());
                principalStage.showAndWait(); 
            } catch (IOException ex) {
                Logger.getLogger(MovimientosController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });  
        
        btnAgregarPromos.setOnAction((ActionEvent e) -> {       
            Parent principal;
            try {
                principal = FXMLLoader.load(getClass().getResource("/xxcell/view/Promociones.fxml"));
                Stage principalStage = new Stage();
                scene = new Scene(principal);
                principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
                principalStage.setScene(scene);
                principalStage.initModality(Modality.APPLICATION_MODAL);
                principalStage.initOwner(btnPrestamos.getScene().getWindow());
                principalStage.showAndWait(); 
            } catch (IOException ex) {
                Logger.getLogger(MovimientosController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });  
        
        btnSalidas.setOnAction((ActionEvent e) -> {       
            Parent principal;
            try {
                principal = FXMLLoader.load(getClass().getResource("/xxcell/view/SalidasPendientes.fxml"));
                Stage principalStage = new Stage();
                principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
                scene = new Scene(principal);
                principalStage.setScene(scene);
                principalStage.initModality(Modality.APPLICATION_MODAL);
                principalStage.initOwner(btnPrestamos.getScene().getWindow());
                principalStage.showAndWait(); 
            } catch (IOException ex) {
                Logger.getLogger(MovimientosController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });  
    }        
}
