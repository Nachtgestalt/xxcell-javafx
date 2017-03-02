package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xxcell.Conexion.Conexion;
import xxcell.model.Productos;
import org.controlsfx.control.textfield.TextFields;
import static xxcell.controller.LoginController.scene;
import xxcell.model.LogReport;


public class ConsultasController implements Initializable {
    Conexion conn = new Conexion();   
    Productos aux = new Productos();
    
    String sqlStmt = "SELECT * FROM productos";
    String StmtSq;
    
    @FXML
    private JFXComboBox<String> Tipo;

    @FXML
    private JFXTextField buscar;
    @FXML
    private JFXComboBox<String> Marca;
    @FXML
    private JFXComboBox<String> cmbModelo;
    @FXML
    private JFXComboBox<String> cmbIdentificador;
    
    Set<String> HSAutocomplete = new HashSet<>();
    
    //botones
    @FXML
    private JFXButton filtrar;
    @FXML
    private JFXButton reset;
    
    //Variables para la Tabla creada
    @FXML
    private TableView<Productos> Tabla;
    @FXML
    private TableColumn<Productos, String> IDCol; //COLUMNA ID
    @FXML
    private TableColumn<Productos, String> ModCol; //COLUMNA MODELO
    @FXML
    private TableColumn<Productos, String> MarcaCol; //COLUMNA MARCA
    @FXML
    private TableColumn<Productos, String> DesCol; //COLUMNA DESCRIPCIÓN
    @FXML
    private TableColumn<Productos, Number> PrPubCol; //COLUMNA PRECIO PUBLICO ***
    @FXML
    private TableColumn<Productos, Number> DispCol; //COLUMNA DISPONIBILIDAD ****
    @FXML
    private TableColumn<Productos, String> TipoCol; //COLUMNA TIPO
    @FXML
    private TableColumn<Productos, String> NomCol; //COLUMNA NOMBRE
    @FXML
    private TableColumn<Productos, Number> PrDisCol; //COLUMNA PRECIO DISTRIBUIDOR***
    @FXML
    private TableColumn<Productos, Number> L58Col; //COLUMNA DISPONIBILIDAD EN LOCAL 58 ****
    @FXML
    private TableColumn<Productos, Number> L64Col; //COLUMNA DISPONIBILIDAD EN LOCAL 64****
    @FXML
    private TableColumn<Productos, Number> L127Col; //COLUMNA DISPONIBILIDAD EN LOCAL 127****
    
    
    //Lista para Llenar la tabla de productos;
    ObservableList<Productos> productos = FXCollections.observableArrayList();
    
    LogReport log;
    Window window;
     
     //Función para llenar el TableView con los datos que el usuario indique
    public ObservableList<Productos> ObtenerProd(String STSQL) throws SQLException{
        String Mod, Marc, DI, Nom, Tip, Dep;   
        int Disp, l58, l64, l127;
        double PPub, PDist;
        if(conn.QueryExecute(STSQL))
        {
            while (conn.setResult.next())
            {
                DI = conn.setResult.getString("ID");
                Marc = conn.setResult.getString("Marca");
                Mod = conn.setResult.getString("Modelo");
                Nom = conn.setResult.getString("Identificador");
                PPub = conn.setResult.getDouble("PrecPub");
                PDist = conn.setResult.getDouble("PrecDist");
                Tip = conn.setResult.getString("Tipo");
                Dep = conn.setResult.getString("Descrip");
                Disp = conn.setResult.getInt("CantidadActual");
                l58 = conn.setResult.getInt("L58");
                l64 = conn.setResult.getInt("L64");
                l127 = conn.setResult.getInt("L127");
                productos.add(new Productos(DI,Marc,Mod,Nom,PPub,PDist,Tip,Dep,Disp,l58,l64,l127));
            }    
        }
        return productos;
    }
    
    public void Autocompletar()
    {
        String query;
        query = "SELECT DISTINCT Modelo FROM productos WHERE Modelo LIKE '%" + buscar.getText() + "%'";
        if(conn.QueryExecute(query)){
            try {
                while(conn.setResult.next()){
                    HSAutocomplete.add(conn.setResult.getString("Modelo"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
    }    
     //Inicia los elementos de los ComboBox para las Marcas y los tipos de Objetos
    public void iniciarComboBox()
    {
        String query;
        cmbModelo.setVisible(false);
        cmbIdentificador.setVisible(false);
        //inicia el combobox Marca
        query = "SELECT DISTINCT Marca FROM productos ORDER BY Marca";
        if(conn.QueryExecute(query))
        {
            try {
                while(conn.setResult.next()){
                    Marca.getItems().add(conn.setResult.getString("Marca"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
        query = "SELECT DISTINCT Tipo FROM productos ORDER BY Tipo";
        if(conn.QueryExecute(query))
        {
            try {
                while(conn.setResult.next()){
                    Tipo.getItems().add(conn.setResult.getString("Tipo"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
    }
    //Inicia los combobox ocultos para Identificador y Modelo
    public void initCmbOcultos(String campo){
        String query;
        switch (campo){
            case "Modelo":
                cmbModelo.getSelectionModel().clearSelection();
                cmbModelo.getItems().clear();
                cmbModelo.setVisible(true);
                query = "SELECT DISTINCT Modelo FROM productos Where Marca = '"+Marca.getValue()+"' ORDER BY Modelo";
                if(conn.QueryExecute(query))
                {
                    try {
                            while(conn.setResult.next()){
                            cmbModelo.getItems().add(conn.setResult.getString("Modelo"));
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                        String msjHeader = "¡Error!";
                        String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                        log.SendLogReport(ex, msjHeader, msjText);
                    }
                }
            break;
            case "Identificador":
                cmbIdentificador.getSelectionModel().clearSelection();
                cmbIdentificador.getItems().clear();
                cmbIdentificador.setVisible(true);
                query = "SELECT DISTINCT Identificador FROM productos Where Tipo = '"+Tipo.getValue()+"' ORDER BY Identificador";
                if(conn.QueryExecute(query))
                {
                    try {
                            while(conn.setResult.next()){
                                cmbIdentificador.getItems().add(conn.setResult.getString("Identificador"));
                            }
                    } catch (SQLException ex) {
                        Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                        String msjHeader = "¡Error!";
                        String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                        log.SendLogReport(ex, msjHeader, msjText);
                    }
                }
            break;
        }
    }
    
    public void filtrar(){
        String query;
        
        if(Marca.getValue() != null && Tipo.getValue() != null && cmbIdentificador.getValue() == null && cmbModelo.getValue() == null){
            query = "Select * FROM productos ";
            query += "WHERE Tipo='"+Tipo.getValue()+"' AND Marca = '"+Marca.getValue()+"'";
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(query));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
        
        if(Marca.getValue() != null && Tipo.getValue() == null && cmbIdentificador.getValue() == null && cmbModelo.getValue() != null){
            query = "Select * FROM productos ";
            query += "WHERE Modelo='"+cmbModelo.getValue()+"' AND Marca = '"+Marca.getValue()+"'";
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(query));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
        if(Marca.getValue() == null && Tipo.getValue() != null && cmbIdentificador.getValue() != null && cmbModelo.getValue() == null){
            query = "Select * FROM productos ";
            query += "WHERE Tipo='"+Tipo.getValue()+"' AND Identificador = '"+cmbIdentificador.getValue()+"'";
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(query));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
        if(Marca.getValue() != null && Tipo.getValue() != null && cmbIdentificador.getValue() != null && cmbModelo.getValue() != null){
            query = "Select * FROM productos ";
            query += "WHERE Tipo='"+Tipo.getValue()+"' AND Identificador = '"+cmbIdentificador.getValue()+"' ";
            query += "AND Modelo='"+cmbModelo.getValue()+"' AND Marca = '"+Marca.getValue()+"'";
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(query));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
        if(Marca.getValue() != null && Tipo.getValue() != null && cmbIdentificador.getValue() != null && cmbModelo.getValue() == null){
            query = "Select * FROM productos ";
            query += "WHERE Tipo='"+Tipo.getValue()+"' AND Identificador = '"+cmbIdentificador.getValue()+"' ";
            query += " AND Marca = '"+Marca.getValue()+"'";
            try {   
                Tabla.refresh();
                System.out.println(query);
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(query));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Autocompletar();
        Platform.runLater(() -> {
            window = filtrar.getScene().getWindow();             
            log = new LogReport(window);
            IDCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            ModCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            MarcaCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            DesCol.prefWidthProperty().bind(Tabla.widthProperty().divide(7)); // w * 1/5
            PrDisCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            PrPubCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            DispCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            TipoCol.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            NomCol.prefWidthProperty().bind(Tabla.widthProperty().divide(7)); // w * 1/5
            L58Col.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            L64Col.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
            L127Col.prefWidthProperty().bind(Tabla.widthProperty().divide(14)); // w * 1/5
        });
        iniciarComboBox();
        Tabla.setPlaceholder(new Label("No hay Coincidencias en la busqueda"));
        IDCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        MarcaCol.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
        ModCol.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        DesCol.setCellValueFactory(cellData -> cellData.getValue().descripProperty());
        TipoCol.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        NomCol.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        PrPubCol.setCellValueFactory(cellData -> cellData.getValue().preciopubProperty());
        PrDisCol.setCellValueFactory(cellData -> cellData.getValue().preciodistProperty());
        //Altera los cells para alertar si algún local no tiene productos, rojo si es 0, amarillo si son 2 o 1
        DispCol.setCellValueFactory(cellData -> cellData.getValue().disponProperty());
        DispCol.setCellFactory( column -> {
            return new TableCell<Productos, Number>(){
                @Override
                protected void updateItem(Number num, boolean empty){
                    super.updateItem(num, empty);
                    
                    if(num == null){
                        setText("");
                        setStyle("");
                    }else{
                        setText(String.valueOf(num));
                        if(num.intValue() <= 0){
                            setTextFill(Color.BLACK);
                            setStyle("-fx-background-color: #FF9980");
                        }
                        if(num.intValue() < 3 && num.intValue() > 0){
                            setTextFill(Color.CHOCOLATE);
                            setStyle("-fx-background-color: #FFFF9F");
                        }
                    }
                }
            };
        });
        L58Col.setCellValueFactory(cellData -> cellData.getValue().l58Property());
        L58Col.setCellFactory( column -> {
            return new TableCell<Productos, Number>(){
                @Override
                protected void updateItem(Number num, boolean empty){
                    super.updateItem(num, empty);
                    
                    if(num == null){
                        setText("");
                        setStyle("");
                    }else{
                        setText(String.valueOf(num));
                        if(num.intValue() <= 0){
                            setTextFill(Color.BLACK);
                            setStyle("-fx-background-color: #FF9980");
                        }
                        if(num.intValue() < 2 && num.intValue() > 0){
                            setTextFill(Color.CHOCOLATE);
                            setStyle("-fx-background-color: #FFFF9F");
                        }
                    }
                }
            };
        });
        
        L64Col.setCellValueFactory(cellData -> cellData.getValue().l64Property());
        L64Col.setCellFactory( column -> {
            return new TableCell<Productos, Number>(){
                @Override
                protected void updateItem(Number num, boolean empty){
                    super.updateItem(num, empty);
                    
                    if(num == null){
                        setText("");
                        setStyle("");
                    }else{
                        setText(String.valueOf(num));
                        if(num.intValue() <= 0){
                            setTextFill(Color.BLACK);
                            setStyle("-fx-background-color: #FF9980");
                        }
                        if(num.intValue() < 2 && num.intValue() > 0){
                            setTextFill(Color.CHOCOLATE);
                            setStyle("-fx-background-color: #FFFF9F");
                        }
                    }
                }
            };
        });
       
        L127Col.setCellValueFactory(cellData -> cellData.getValue().l127Property());
        L127Col.setCellFactory( column -> {
            return new TableCell<Productos, Number>(){
                @Override
                protected void updateItem(Number num, boolean empty){
                    super.updateItem(num, empty);
                    
                    if(num == null){
                        setText("");
                        setStyle("");
                    }else{
                        setText(String.valueOf(num));
                        if(num.intValue() <= 0){
                            setTextFill(Color.BLACK);
                            setStyle("-fx-background-color: #FF9980");
                        }
                        if(num.intValue() < 2 && num.intValue() > 0){
                            setTextFill(Color.CHOCOLATE);
                            setStyle("-fx-background-color: #FFFF9F");
                        }
                    }
                }
            };
        });
        
        //Función: ToolTip para mostrar la descripción de producto
        Tabla.setRowFactory(tv -> new TableRow<Productos>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Productos Prod, boolean empty) {
                super.updateItem(Prod, empty);
                if (Prod == null) {
                    setTooltip(null);
                } else {
                    if(Prod.getDescrip().length() > 0){
                        tooltip.setText(Prod.getDescrip());
                        setTooltip(tooltip);
                    }
                }
            }
        });
        
        //Función para mostrar imagen si tiene con doble click
        Tabla.setOnMousePressed(new EventHandler<MouseEvent>() {
             @Override 
             public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    if(Tabla.getSelectionModel().getSelectedItem() != null ){
                        aux = Tabla.getSelectionModel().getSelectedItem();
                        try {
                            mostrarImagen();
                        } catch (IOException ex) {
                            Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                            String msjHeader = "¡Error!";
                            String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                            log.SendLogReport(ex, msjHeader, msjText);
                        }
                    }
                }
            }
        });
        
        //Función para el comboBox Marca; Esté llenara el TableView dependiendo la Opción que el usuario Ingrese
        Marca.setOnAction(e -> {
            buscar.clear();
            StmtSq = "Select * FROM productos "; 
            StmtSq += "WHERE Marca='"+Marca.getValue()+"'";
            initCmbOcultos("Modelo");
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(StmtSq));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        });//FIN DE LAMBDA MARCA       
        
        //Función para el combobox Tipo
        Tipo.setOnAction(e -> {
            buscar.clear();
            StmtSq = "Select * FROM productos "; 
            StmtSq += "WHERE Tipo='"+Tipo.getValue()+"'";
            initCmbOcultos("Identificador");
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(StmtSq));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            } 
        });//FIN DE LAMBDA   
        
        //Función para el autocompletado del Textfield Buscar
        buscar.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            StmtSq = "Select * FROM productos ";
            StmtSq += "WHERE Modelo = '"+buscar.getText()+"'";
            try {   
                Tabla.refresh();
                productos.removeAll(productos);
                Tabla.setItems(ObtenerProd(StmtSq));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                String msjHeader = "¡Error!";
                String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                log.SendLogReport(ex, msjHeader, msjText);
            }
        }); 
        TextFields.bindAutoCompletion(buscar, HSAutocomplete);
        
        filtrar.setOnAction((ActionEvent e) -> { 
            filtrar();
        });
        
        reset.setOnAction((ActionEvent e) -> { 
            sqlStmt = "SELECT * FROM productos";
            Tabla.refresh();
            productos.removeAll(productos);
            Marca.getSelectionModel().clearSelection();
            Marca.getItems().clear();
            Tipo.getSelectionModel().clearSelection();
            Tipo.getItems().clear();
            cmbIdentificador.getSelectionModel().clearSelection();
            cmbIdentificador.getItems().clear();
            cmbModelo.getSelectionModel().clearSelection();
            cmbModelo.getItems().clear();
            iniciarComboBox();
        });//FIN RESET
    }//Fin de INIT    
    
    public void mostrarImagen() throws IOException{
        Variables_Globales.producto = aux;
        String query = "SELECT imagenProducto FROM productos WHERE ID = '"+aux.getID()+"'";
        conn.QueryExecute(query);
        try {
            if(conn.setResult.first()) {
                if(conn.setResult.getBlob("ImagenProducto") != null){
                    try {
                        Parent principal;
                        principal = FXMLLoader.load(getClass().getResource("/xxcell/view/PopUpImagen.fxml"));
                        Stage principalStage = new Stage();
                        principalStage.getIcons().add(new Image("/xxcell/Images/XXCELL450.png"));
                        scene = new Scene(principal);
                        principalStage.setScene(scene);
                        principalStage.initModality(Modality.APPLICATION_MODAL);
                        principalStage.setTitle("Imagen de Producto");
                        principalStage.initOwner(reset.getScene().getWindow());
                        principalStage.showAndWait(); 
                    } catch (IOException ex) {
                        Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
                        String msjHeader = "¡Error!";
                        String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
                        log.SendLogReport(ex, msjHeader, msjText);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
            String msjHeader = "¡Error!";
            String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
            log.SendLogReport(ex, msjHeader, msjText); 
        }
    }
}
