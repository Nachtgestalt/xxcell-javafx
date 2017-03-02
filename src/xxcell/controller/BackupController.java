/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author snak0
 */
public class BackupController implements Initializable {

    @FXML
    private JFXTextField txtFilePath;

    @FXML
    private JFXButton btnFile;
    
    @FXML
    private JFXButton btnRespaldar;

    @FXML
    private JFXRadioButton rdbtnRespaldar = new JFXRadioButton("Respaldar");

    @FXML
    private JFXRadioButton rdbtnRestaurar;
    
    @FXML
    private Label lblSuccess;  
    
    String path = "";
    String filename;

    @FXML
    void ActionRespaldar(ActionEvent event) {
        Process proceso = null;
        String mensaje = null;
        String user = "root";
        String pass = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            if(rdbtnRespaldar.isSelected()){
                if(!txtFilePath.getText().isEmpty()){
                    proceso = runtime.exec("C:/xampp/mysql/bin/mysqldump.exe -uroot --add-drop-database -B xxcell -r"+path);
                    mensaje = "Respaldar ";
                } else
                    lblSuccess.setText("Debe seleccionar una dirección para respaldar");
            } else if(rdbtnRestaurar.isSelected()){
                if(!txtFilePath.getText().isEmpty()){
                    String[] restoreCmd = new String[]{"C:/xampp/mysql/bin/mysql.exe", "--user="+user,"--password="+pass,"-e","source "+path};
                    proceso = runtime.exec(restoreCmd);
                    mensaje = "Restaurar ";
                } else
                    lblSuccess.setText("Debe seleccionar un archivo para restaurar");
            }
            
            int processComplete = proceso.waitFor();
            if(processComplete == 0)
                lblSuccess.setText("Exito al "+mensaje);
            else
                lblSuccess.setText("Error al "+mensaje);
                
        } catch (Exception e) {
        }        
    }
    
     @FXML
    void ActionCancelar(ActionEvent event) {
        Stage stage;
        stage = (Stage) btnRespaldar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void ActionSelectFile(ActionEvent event) {
        DirectoryChooser dChooser = new DirectoryChooser();
        FileChooser fChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterSQL= new FileChooser.ExtensionFilter("sql files (*.sql)", "*.SQL");
        fChooser.getExtensionFilters().addAll(extFilterSQL);
        File file;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if(rdbtnRespaldar.isSelected()){
            file = dChooser.showDialog(btnFile.getScene().getWindow());
            if(file!=null){
                path = file.getAbsolutePath();
                path = path.replace('\\', '/');
                path = path + "/xxcellDB_" + date + ".sql";
            }
        } else{
            file = fChooser.showOpenDialog(btnFile.getScene().getWindow());
            if(file!=null){
                path = file.getAbsolutePath();
                path = path.replace('\\', '/');
            }
        }
            
        if(file!=null){
            txtFilePath.setText(path);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        Platform.runLater(() -> {
        });
        
        final ToggleGroup group = new ToggleGroup();
        rdbtnRespaldar.setUserData("Respaldar");
        rdbtnRestaurar.setUserData("Restaurar");
        
        rdbtnRespaldar.setToggleGroup(group);
        rdbtnRespaldar.setSelected(true);
        btnRespaldar.setText("Respaldar");
        rdbtnRestaurar.setToggleGroup(group);
        
        group.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            String mensaje = null;
            if (group.getSelectedToggle() != null) {
                txtFilePath.clear();
                lblSuccess.setText("");
                mensaje = group.getSelectedToggle().getUserData().toString();
                btnRespaldar.setText(mensaje);                
            }
        });
    }    
    
}
