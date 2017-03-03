package xxcell.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import xxcell.Conexion.Conexion;
import xxcell.model.LogReport;
import xxcell.model.Productos;

public class EstadisticasProductosController implements Initializable {
    
    //Conexión a la base de Datos
    Conexion conn = new Conexion();
    
    LogReport log;
    
    //Date pickers, entre que fecha y que fecha se darán las estadisticas
    @FXML
    private JFXDatePicker DatePInicio;
    @FXML
    private JFXDatePicker DatePFin;
    LocalDate FechaInit, FinalFecha;

    //Combobox
    @FXML
    private JFXComboBox<String> cmbMarca;
    @FXML
    private JFXComboBox<String> cmbModelo;
    @FXML
    private JFXComboBox<String> cmbTipo;

    //Botones
    @FXML
    private JFXButton btnAlmacen;
    @FXML
    private JFXButton btnReset;
    @FXML
    private JFXButton btnAceptar;

    //BarChart, muestrá de forma gráfica las estadisticas
    @FXML
    private PieChart pieChartProductos;
    ObservableList<PieChart.Data> pieChartDataTipo = FXCollections.observableArrayList();
    //BarChart
    @FXML
    private PieChart pieChartMarca;
    ObservableList<PieChart.Data> pieChartDataMarca = FXCollections.observableArrayList();

    //Tabla para mostrar el número de ventas que se realizo por producto
    @FXML
    private TableView<Productos> tblProductos;
    @FXML
    private TableColumn<Productos, String> colCodigo;
    @FXML
    private TableColumn<Productos, String> colModelo;
    @FXML
    private TableColumn<Productos, String> colTipo;
    @FXML
    private TableColumn<Productos, String> colIdentificador;
    @FXML
    private TableColumn<Productos, Number> colNumVentas;
    @FXML
    private TableColumn<Productos, String> colMarca;
    //Lista para Llenar la tabla de productos;
    ObservableList<Productos> productos = FXCollections.observableArrayList();
    //Lista para llenar el PieChart
    ObservableList<Productos> dataProductosTipo = FXCollections.observableArrayList();
    ObservableList<Productos> dataProductosMarca = FXCollections.observableArrayList();
    
    public ObservableList<Productos> ObtenerProd() throws SQLException{
        String STSQL = "SELECT productos.ID, productos.Modelo, productos.Tipo, productos.Marca, "
                + "productos.Identificador, tblventadetalle.ventaCantidad "
                + "FROM productos " 
                + "INNER JOIN tblventadetalle "
                + "ON productos.ID = tblventadetalle.productoCodigo "
                + "INNER JOIN tblventas "
                + "ON tblventas.ventaFolio = tblventadetalle.ventaFolio "
                + "WHERE tblventas.ventaFecha BETWEEN '"+FechaInit+"' and '"+FinalFecha.plusDays(1)+"'";
        System.out.println(STSQL);
        boolean acceselse, accesMarca, accessTipo;
        String Mod, Marc, DI, Nom, Tip;   
        int VentaCantidad;
        if(conn.QueryExecute(STSQL))
        {
            while (conn.setResult.next())
            {
                DI = conn.setResult.getString("ID");
                Marc = conn.setResult.getString("Marca");
                Mod = conn.setResult.getString("Modelo");
                Nom = conn.setResult.getString("Identificador");
                Tip = conn.setResult.getString("Tipo");
                VentaCantidad = conn.setResult.getInt("ventaCantidad");
                if(productos.isEmpty()){
                    productos.add(new Productos(DI, Marc, Mod, Nom, Tip, VentaCantidad));
                }else{
                    acceselse = true;
                    for(int i = 0 ; i<productos.size() ; i++){
                        if(productos.get(i).getID().equals(DI)){
                           productos.get(i).setventasTotales(VentaCantidad);
                           acceselse = false;
                        }  
                    }
                    if(acceselse){
                        productos.add(new Productos(DI, Marc, Mod, Nom, Tip, VentaCantidad));
                    }
                }
                //Función para llenar el dataProductos para Piechart Tipo
                if(dataProductosTipo.isEmpty()){
                    dataProductosTipo.add(new Productos(Tip, VentaCantidad));
                }else{
                    accesMarca = true;
                    for(int i = 0 ; i<dataProductosTipo.size() ; i++){
                        if(dataProductosTipo.get(i).getTipo().equals(Tip)){
                           dataProductosTipo.get(i).setventasTotales(VentaCantidad);
                           accesMarca = false;
                        }  
                    }
                    if(accesMarca){
                        dataProductosTipo.add(new Productos(Tip, VentaCantidad));
                    }
                }
                //Función para llenar el dataProductos para Piechart Marca
                if(dataProductosMarca.isEmpty()){
                    dataProductosMarca.add(new Productos(VentaCantidad, Marc));
                }else{
                    accessTipo = true;
                    for(int i = 0 ; i<dataProductosMarca.size() ; i++){
                        if(dataProductosMarca.get(i).getMarca().equals(Marc)){
                           dataProductosMarca.get(i).setventasTotales(VentaCantidad);
                           accessTipo = false;
                        }  
                    }
                    if(accessTipo){
                        dataProductosMarca.add(new Productos(VentaCantidad, Marc));
                    }
                }
            }//Para evitar confusiones: ¡Fin While!    
        }
        for(int i = 0 ; i<dataProductosTipo.size() ; i++){
            pieChartDataTipo.add(new PieChart.Data(dataProductosTipo.get(i).getTipo(), dataProductosTipo.get(i).getventasTotales()));      
        }
        for(int i = 0 ; i<dataProductosMarca.size() ; i++){
            pieChartDataMarca.add(new PieChart.Data(dataProductosMarca.get(i).getMarca(), dataProductosMarca.get(i).getventasTotales()));      
        }       
        int x;
        pieChartDataTipo.forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " ", data.pieValueProperty(), ""
                )
            )
              
        );
        pieChartDataMarca.forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " ", data.pieValueProperty(), ""
                )
            )
        );
        return productos; 
    }
    
    public ObservableList<Productos> ObtenerProdComboBox(String combobox, int indicador) throws SQLException{
        String STSQL = "SELECT productos.ID, productos.Modelo, productos.Tipo, productos.Marca, "
                + "productos.Identificador, tblventadetalle.ventaCantidad "
                + "FROM productos " 
                + "INNER JOIN tblventadetalle "
                + "ON productos.ID = tblventadetalle.productoCodigo "
                + "INNER JOIN tblventas "
                + "ON tblventas.ventaFolio = tblventadetalle.ventaFolio ";
        if(indicador == 1){
            STSQL += "AND productos.Marca = '"+combobox+"'";
        }
        if(indicador == 2){
            STSQL += "AND productos.Tipo = '"+combobox+"'";
        }
        if(indicador == 3){
            STSQL += "AND productos.Modelo = '"+combobox+"'";
        }
        STSQL += " WHERE tblventas.ventaFecha BETWEEN '"+FechaInit+"' and '"+FinalFecha.plusDays(1)+"'";
        
        boolean acceselse, accesMarca, accessTipo;
        String Mod, Marc, DI, Nom, Tip;   
        int VentaCantidad;
        if(conn.QueryExecute(STSQL))
        {
            while (conn.setResult.next())
            {
                DI = conn.setResult.getString("ID");
                Marc = conn.setResult.getString("Marca");
                Mod = conn.setResult.getString("Modelo");
                Nom = conn.setResult.getString("Identificador");
                Tip = conn.setResult.getString("Tipo");
                VentaCantidad = conn.setResult.getInt("ventaCantidad");
                if(productos.size() == 0){
                    productos.add(new Productos(DI, Marc, Mod, Nom, Tip, VentaCantidad));
                }else{
                    acceselse = true;
                    for(int i = 0 ; i<productos.size() ; i++){
                        if(productos.get(i).getID().equals(DI)){
                           productos.get(i).setventasTotales(VentaCantidad);
                           acceselse = false;
                        }  
                    }
                    if(acceselse){
                        productos.add(new Productos(DI, Marc, Mod, Nom, Tip, VentaCantidad));
                    }
                } 
                //Función para llenar el dataProductos para Piechart Tipo
                if(dataProductosTipo.isEmpty()){
                    dataProductosTipo.add(new Productos(Tip, VentaCantidad));
                }else{
                    accesMarca = true;
                    for(int i = 0 ; i<dataProductosTipo.size() ; i++){
                        if(dataProductosTipo.get(i).getTipo().equals(Tip)){
                           dataProductosTipo.get(i).setventasTotales(VentaCantidad);
                           accesMarca = false;
                        }  
                    }
                    if(accesMarca){
                        dataProductosTipo.add(new Productos(Tip, VentaCantidad));
                    }
                }
                //Función para llenar el dataProductos para Piechart Marca
                if(dataProductosMarca.isEmpty()){
                    dataProductosMarca.add(new Productos(VentaCantidad, Marc));
                }else{
                    accessTipo = true;
                    for(int i = 0 ; i<dataProductosMarca.size() ; i++){
                        if(dataProductosMarca.get(i).getMarca().equals(Marc)){
                           dataProductosMarca.get(i).setventasTotales(VentaCantidad);
                           accessTipo = false;
                        }  
                    }
                    if(accessTipo){
                        dataProductosMarca.add(new Productos(VentaCantidad, Marc));
                    }
                }
            }    
        }
        for(int i = 0 ; i<dataProductosTipo.size() ; i++){
            pieChartDataTipo.add(new PieChart.Data(dataProductosTipo.get(i).getTipo(), dataProductosTipo.get(i).getventasTotales()));      
        }
        for(int i = 0 ; i<dataProductosMarca.size() ; i++){
            pieChartDataMarca.add(new PieChart.Data(dataProductosMarca.get(i).getMarca(), dataProductosMarca.get(i).getventasTotales()));      
        }       
        int x;
        pieChartDataTipo.forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " ", data.pieValueProperty(), ""
                )
            )
              
        );
        pieChartDataMarca.forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " ", data.pieValueProperty(), ""
                )
            )
        );
        return productos;
    }
    
    public void iniciarComboBox()
    {
        inicializar(true);
        String query = "SELECT DISTINCT Marca FROM productos ORDER BY Marca";
        
        if(conn.QueryExecute(query))
        {
            try {
                while(conn.setResult.next()){
                    cmbMarca.getItems().add(conn.setResult.getString("Marca"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        query = "SELECT DISTINCT Tipo FROM productos ORDER BY Tipo";
        if(conn.QueryExecute(query))
        {
            try {
                while(conn.setResult.next()){
                    cmbTipo.getItems().add(conn.setResult.getString("Tipo"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void iniciarComboboxOcultos(){
        String query;
        cmbModelo.getSelectionModel().clearSelection();
        cmbModelo.getItems().clear();
        cmbModelo.setVisible(true);
        query = "SELECT DISTINCT Modelo FROM productos Where Marca = '"+cmbMarca.getValue()+"' ORDER BY Modelo";
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
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Iniciar Combobox
        iniciarComboBox();
        //Inicializa las columnas de la tabla
        colCodigo.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colMarca.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
        colModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colIdentificador.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colNumVentas.setCellValueFactory(cellData -> cellData.getValue().ventasTotalesProperty());

        //Funciones de Combobox
        cmbMarca.setOnAction(e -> {
            clearPieChart();
            iniciarComboboxOcultos();
            try {   
                tblProductos.refresh();
                productos.removeAll(productos);
                tblProductos.setItems(ObtenerProdComboBox(cmbMarca.getValue(), 1));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        cmbModelo.setOnAction(e -> {
            clearPieChart();
            try {   
                tblProductos.refresh();
                productos.removeAll(productos);
                tblProductos.setItems(ObtenerProdComboBox(cmbModelo.getValue(), 3));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        cmbTipo.setOnAction(e -> {
            clearPieChart();
            try {   
                tblProductos.refresh();
                productos.removeAll(productos);
                tblProductos.setItems(ObtenerProdComboBox(cmbTipo.getValue(), 2));
            } catch (SQLException ex) {
                Logger.getLogger(ConsultasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
        
        //Funcón para botón "Aceptar"
        btnAceptar.setOnAction((ActionEvent e) -> {       
            if(DatePInicio.getValue() != null && DatePFin.getValue() != null){
                FechaInit = DatePInicio.getValue();
                FinalFecha = DatePFin.getValue();
                inicializar(false);
                //Inicializa la Tabla y piecharts
                try {
                    tblProductos.setItems(ObtenerProd());
                    pieChartMarca.setData(pieChartDataMarca);
                    pieChartMarca.setLabelLineLength(10);
                    pieChartMarca.setLegendSide(Side.TOP);
                    pieChartProductos.setData(pieChartDataTipo);
                    pieChartProductos.setLabelLineLength(10);
                    pieChartProductos.setLegendSide(Side.TOP);
                } catch (SQLException ex) {
                    Logger.getLogger(ModificarAlmacenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
        btnReset.setOnAction((ActionEvent e) -> {       
            reset();
        });
        btnAlmacen.setOnAction((ActionEvent e) -> { 
            try {
                clearPieChart();
                tblProductos.setItems(ObtenerProd());
                pieChartMarca.setData(pieChartDataMarca);
                pieChartMarca.setLabelLineLength(10);
                pieChartMarca.setLegendSide(Side.TOP);
                pieChartProductos.setData(pieChartDataTipo);
                pieChartProductos.setLabelLineLength(10);
                pieChartProductos.setLegendSide(Side.TOP);
            } catch (SQLException ex) {
                Logger.getLogger(ModificarAlmacenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void inicializar(boolean flag){
        cmbMarca.setDisable(flag);
        cmbModelo.setDisable(flag);
        cmbTipo.setDisable(flag);
        btnAlmacen.setDisable(flag);
        btnReset.setDisable(flag);
    }
    
    public void clearPieChart(){
        productos.removeAll(productos);
        pieChartDataTipo.removeAll(pieChartDataTipo);
        pieChartProductos.setData(pieChartDataTipo);
        pieChartDataMarca.removeAll(pieChartDataMarca);
        pieChartMarca.setData(pieChartDataMarca);
        dataProductosMarca.removeAll(dataProductosMarca);
        dataProductosTipo.removeAll(dataProductosTipo);
    }
    
    public void reset(){
        inicializar(true);
        cmbMarca.getSelectionModel().clearSelection();
        cmbMarca.getItems().clear();
        cmbModelo.getSelectionModel().clearSelection();
        cmbModelo.getItems().clear();
        cmbTipo.getSelectionModel().clearSelection();
        cmbTipo.getItems().clear();
        iniciarComboBox();
        DatePFin.setValue(null);
        DatePInicio.setValue(null);
        tblProductos.refresh();
        productos.removeAll(productos);
        pieChartDataTipo.removeAll(pieChartDataTipo);
        pieChartProductos.setData(pieChartDataTipo);
        pieChartMarca.setData(pieChartDataMarca);
        dataProductosMarca.removeAll(dataProductosMarca);
        dataProductosTipo.removeAll(dataProductosTipo);
    }
}
