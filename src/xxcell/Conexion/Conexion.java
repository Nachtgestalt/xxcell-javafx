package xxcell.Conexion;
    
import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {
        /*public static Connection conector() throws ClassNotFoundException, SQLException
        {
            try
            {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/xxcell","root","");
		return conn;
            } 
            catch(Exception e)
            {
                return null;
            }
        }*/
    static Connection conexion;
    public Connection jasperconexion;
    static String query;
    Statement sqlStmt;
    public ResultSet setResult;
    public PreparedStatement stmt;

    //Conexión para Jasper Reports
    public Connection JasperConexion() throws SQLException {
            //CONEXIÓN LOCALHOLST
        this.jasperconexion = DriverManager.getConnection("jdbc:mysql://localhost/xxcell","root","");
            //CONEXIÓN LOCAL 58
        //this.jasperconexion = DriverManager.getConnection("jdbc:mysql://192.168.1.64:3306/xxcell","Local58","xxcell");
            //CONEXIÓN LOCAL 64
        //this.jasperconexion = DriverManager.getConnection("jdbc:mysql://192.168.1.64:3306/xxcell","Local58","xxcell");    
        return jasperconexion;
    }
    

    public void preparedStatement(String sql) throws SQLException {
        stmt = conexion.prepareStatement(sql);
    }
    
    public boolean ConnectionOpen()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/xxcell","root","");
            //conexion = DriverManager.getConnection("jdbc:mysql://192.168.1.64:3306/xxcell","Local58","xxcell");
            //conexion = DriverManager.getConnection("jdbc:mysql://192.168.1.64:3306/xxcell","Local64","xxcell");
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
	}
    }

    void ConnectionClose()
    {
	try{
            conexion.close();
	} catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
	
    public boolean QueryExecute(String sqlQuery)
    {
        try{
	sqlStmt = conexion.createStatement();
	} catch(Exception e){
            return false;
	}
	try{
            setResult = sqlStmt.executeQuery(sqlQuery);
            return true;
	} catch(SQLException e){
            return false;
	}		
    }
    
	
    public boolean QueryUpdate(String sqlQuery)
    {
        try{
            //stmt = conexion.prepareStatement(sqlQuery);
            sqlStmt = conexion.createStatement();
	} catch(Exception e){
            return false;
	}
	try{
            sqlStmt.executeUpdate(sqlQuery);
            return true;
	} catch(Exception e){
            JOptionPane.showMessageDialog(null, "Query Update: "+e.getMessage().toString());
            return false;
	}
    }
	
    public void AutoCommit(boolean commit){
		try{
			conexion.setAutoCommit(commit);
		} catch(SQLException e){}
	}
	
    public void RollBack(){
		try{
			conexion.rollback();
		} catch(SQLException e){}
	}
	
    public void Commit(){
		try{
			conexion.commit();
		} catch(SQLException e){}
	}
}
