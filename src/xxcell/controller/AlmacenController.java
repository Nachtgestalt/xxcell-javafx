package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import static xxcell.controller.LoginController.scene;

public class AlmacenController implements Initializable {
    
    @FXML
    private JFXButton AgregProd;
    
    @FXML
    private JFXButton btnRespaldar;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    void AddAction(ActionEvent event) throws IOException {
        Parent principal;
        principal = FXMLLoader.load(getClass().getResource("/xxcell/view/AgregarProducto.fxml"));
        Stage principalStage = new Stage();
        scene = new Scene(principal);
        principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
        principalStage.setScene(scene);
        principalStage.initModality(Modality.APPLICATION_MODAL);
        principalStage.setResizable(false);
        principalStage.setTitle("XXCELL - Agregar producto");
        principalStage.initOwner(AgregProd.getScene().getWindow());
        principalStage.showAndWait(); 
    }
    
    @FXML
    void ActionRespaldo(ActionEvent event) throws IOException {
        Parent principal;
        principal = FXMLLoader.load(getClass().getResource("/xxcell/view/Backup.fxml"));
        Stage principalStage = new Stage();
        principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
        scene = new Scene(principal);
        principalStage.setScene(scene);
        principalStage.initModality(Modality.APPLICATION_MODAL);
        principalStage.setResizable(false);
        principalStage.initOwner(AgregProd.getScene().getWindow());
        principalStage.showAndWait(); 
    }
    
    @FXML
    void ActionEntradas(ActionEvent event) throws IOException {
        Parent principal;
        principal = FXMLLoader.load(getClass().getResource("/xxcell/view/EntradasInventario.fxml"));
        Stage principalStage = new Stage();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double origH = primaryScreenBounds.getHeight();
        scene = new Scene(principal);
        principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
        principalStage.setScene(scene);
        principalStage.initModality(Modality.APPLICATION_MODAL);
        principalStage.setResizable(false);
        principalStage.setTitle("XXCELL - Entrada de inventario");
        principalStage.initOwner(AgregProd.getScene().getWindow());
        principalStage.showAndWait(); 
    }
        
    private void cargarScene(String path, String title) throws IOException{
        Parent principal;
        principal = FXMLLoader.load(getClass().getResource(path));
        Stage principalStage = new Stage();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double origH = primaryScreenBounds.getHeight();
        scene = new Scene(principal);
        principalStage.setScene(scene);
        principalStage.initModality(Modality.APPLICATION_MODAL);
        principalStage.setResizable(false);
        principalStage.setTitle("XXCELL - "+title+"");
        principalStage.initOwner(AgregProd.getScene().getWindow());
        principalStage.showAndWait(); 
    }
    
    @FXML
    void ActionGaleria(ActionEvent event) throws IOException {
        Parent principal;
        principal = FXMLLoader.load(getClass().getResource("/xxcell/view/Galery.fxml"));
        Stage principalStage = new Stage();
        principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
        scene = new Scene(principal);
        principalStage.setScene(scene);
        principalStage.initModality(Modality.WINDOW_MODAL);
        principalStage.setResizable(false);
        principalStage.initOwner(AgregProd.getScene().getWindow());
        principalStage.showAndWait(); 
    }
}
