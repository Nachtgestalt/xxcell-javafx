package xxcell.controller;

import xxcell.model.Distribuidores;
import xxcell.model.Empleados;
import xxcell.model.Productos;

public class Variables_Globales {
    static String Rol;      //Rol del usuario
    static String totalVenta;
    static boolean ventaRealizada;
    static Productos BusquedaVenta = new Productos();
    static Productos producto;
    static Empleados empleado;
    static Distribuidores BusquedaDistribuidor = new Distribuidores();
    static String local = "L58";
    static String Columna;
    public static String localPublico = "L58";
    public static String nameImage;
    static double recibida;
    static double cambio;
    //Impresión de Codigo de Barras
    public static String RutaImagenes = "E:\\Codigos\\";
    public static String CodigoProducto = "";
}
