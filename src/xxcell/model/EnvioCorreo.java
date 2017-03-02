package xxcell.model;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.application.Platform;
import javafx.stage.Window;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import xxcell.controller.Variables_Globales;

public class EnvioCorreo {
    
    LogReport log;
    Window window; 
    
    Date fechaHoy = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("yy-MM-dd");
    SimpleDateFormat formatohora = new SimpleDateFormat("hh:mm aa");
    
    String host ="smtp.gmail.com" ;
    String user = "noaydeh@gmail.com";
    String pass = "lacrimamosa123";
    String from = "noaydeh@gmail.com";
    String destinatario = "nachtgestalt06@gmail.com";
    
    long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
    
    public EnvioCorreo(){
         Platform.runLater(()-> {
            //window = btn.getScene().getWindow();             
            log = new LogReport();
        });
    }
    
    public void EnviarCorreo() {
        try{         
            //  Propiedades de la conexión
            Properties props = new Properties();
            // Nombre del host de correo, es smtp.gmail.com
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            // TLS si está disponible
            props.setProperty("mail.smtp.starttls.enable", "true");
            // Puerto de gmail para envio de correos
            props.setProperty("mail.smtp.port","587");
            // Nombre del usuario
            props.setProperty("mail.smtp.user", "noaydeh@gmail.com");
            // Si requiere o no usuario y password para conectarse.
            props.setProperty("mail.smtp.auth", "true");
            
            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);
            
            MimeMessage message = new MimeMessage(session);
            
            // Quien envia el correo
            message.setFrom(new InternetAddress(destinatario));
            // A quien va dirigido
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("noaydeh@hotmail.com"));
            message.setSubject("Cierre de Local " + Variables_Globales.localPublico);
            message.setText("Se cerro a las : " + formato.format(fechaHoy) +" " +formatohora.format(fechaHoy));
            
            Transport t = session.getTransport("smtp");
            t.connect(user,pass);
            t.sendMessage(message,message.getAllRecipients());
            t.close();
            System.out.println("Enviado mensaje sin archivo adjunto");
            
        }catch(MessagingException ex){
            String msjHeader = "¡ERROR!";
            String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
            log.SendLogReport(ex, msjHeader, msjText);
        }
    }
    
    //Correos por PDF
    
    public void EnviarCorreoPDF() {
        try{  
            TInicio = System.currentTimeMillis();
            boolean sessionDebug = false;

            //  Propiedades de la conexión
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            
            //  Instancia de Session
            Session mailSession = Session.getDefaultInstance(props, null);
            //  setDebug(true) para obtener más información por pantalla de lo que está sucediendo
            mailSession.setDebug(sessionDebug);

            //El texto
            BodyPart texto = new MimeBodyPart();
            texto.setText("Local "+Variables_Globales.localPublico+" cerro: " + formato.format(fechaHoy) +" " +formatohora.format(fechaHoy));
            
            //Archivo Adjunto
            BodyPart adjunto = new MimeBodyPart(); //C:\\Users\\XXCELL_L127\\Documents\\Reportes\\VentaDia_127.pdf
            adjunto.setDataHandler(new DataHandler(new FileDataSource("src/xxcell/Reportes/VentaDia_58_"+ formato.format(fechaHoy) +".pdf")));
            // Mi maquína adjunto.setDataHandler(new DataHandler(new FileDataSource("D:\\Punto de Venta\\XCELLBeta2\\src\\xxcell\\Reportes\\VentaDia.pdf")));
            //D:\Punto de Venta\XCELLBeta2\src\xxcell\Reportes\VentaDia.pdf
            //C:\\Users\\Adrián Pérez\\Documents\\Reportes\\VentaDia_127.pdf -- src/xxcell/Reportes/
            adjunto.setFileName("VentaDia.pdf");
            
            //Juntar estas dos partes en una única parte compuesta.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);
            
            // Se compone el correo, dando to, from, subject y el
            // contenido.
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(destinatario));
            message.setSubject("Prueba con Archivo");
            message.setContent(multiParte);
            
            //Para enviar el mensaje usamos la clase Transport, que se obtiene de Session. 
            //El método getTransport() requiere un parámetro String con el nombre del protocolo a usar. 
            //Como el de gmail es smtp, pues ponemos smtp.
            Transport transport=mailSession.getTransport("smtp");
            //Ahora debemos establecer la conexión, dando el nombre de usuario y password.
            transport.connect(host, user, pass);
            //Y ahora simplemente enviamos el mensaje
            transport.sendMessage(message, message.getAllRecipients());
            //cerrar la conexión
            transport.close();
            TFin = System.currentTimeMillis();
            tiempo = TFin - TInicio;
            System.out.println("Tiempo de ejecución en milisegundos: " + tiempo);
            System.out.println("Enviado mensaje con archivo adjunto");
        }catch(MessagingException ex){
            String msjHeader = "¡ERROR!";
            String msjText = "Copiar y mandarlo por correo a noaydeh@hotmail.com";
            log.SendLogReport(ex, msjHeader, msjText);
        }
    }
}
