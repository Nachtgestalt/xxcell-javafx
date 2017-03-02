/* 
    Standarización con XXCELL para generar un Código de 13 Letras o menos
    1   2   3      4   5   6   7       8   9   10        11  12  13
    M A R C A       M O D E L O        T  I  P  O      IDENTIFICADOR
        
 El identificador no será generado en ésta función
 */
package xxcell.model;

public class GeneraCodigos {

    String codigo;
    
    /*
        La clase recibirá una cadena y un entero:
        La cadena será enviada por los Listener de los textfields para la Marca, Modelo y Tipo de los Controllers
        La función tendrá 3 fases dado el orden del código Marca -> Modelo -> Tipo
        Fase 1: Se generan los primeros 3 caracteres de la Marca
        Fase 2: Se generas las siguientes 4 caracteres con respecto al Modelo, 
        Fase 3. Se generan los ultimos 3 caracteres con respecto al Tipo
    
        Nota 1: Los Modelos pueden tener un sólo caracter. Ejemplo: Moto "E", Xperia "M"
        Nota 2: Lo modelos pueden Tener 2 palabras y su vez; sean Palabra_1 y Palabra_2:
                a) Palabra_1 puede Tener un solo caracter Ejemplo. LG "G PRO", MOTOROLA "X PLAY"
                    CODIGO: MMM - 1222 - XXX - XXX ; Dónde 1 son los caracteres de Palabra_1 y 2 los de Palabra_2
                b) Palabra_2 puede Tener un solo caracter Ejemplo. SAMSUNG "ACE 3", NEXUS 6
                    CODIGO: MMM - 1112 - XXX - XXX ; Dónde 1 son los caracteres de Palabra_1 y 2 los de Palabra_2
                c) Palabra_1 y Palabra_2 pueden tener mas de 2 caracteres cada uno.
                    CODIGO: MMM - 1122 - XXX - XXX ; Dónde 1 son los caracteres de Palabra_1 y 2 los de Palabra_2
    */
    
    public GeneraCodigos(){
    
    }
    
    public String GeneraCodigos (String cadenaMarca, String cadenaModelo, String cadenaTipo){
        
        //Fase 1 MARCA
         if(cadenaMarca.length() > 2)
            codigo = cadenaMarca.substring(0, 3);
        else
            codigo = cadenaMarca.substring(0, cadenaMarca.length());
        
         //Fase 2 Modelo
        codigo += Fasedos(cadenaModelo);
        
        //FASE 3 TIPO
        codigo += Fasetres(cadenaTipo);
        
        return codigo;
    }
    
    //Fase 2
    public String Fasedos(String cadenaTextfield){
        String cadena = "", cadenaDerecha, cadenaIzquierda;
        //boolean masdedospalabras = false;
        int[] posiciones = new int[5];
        int palabras = 1, contador = 1, posicion=0, tamano;
        int split;
        
        for(int i = 0; i < cadenaTextfield.length(); i++){
            if(cadenaTextfield.charAt(i) == ' '){
                palabras++;
                posiciones[contador] = posicion;
                contador++;
            }
            posicion++;
        }
        
       /* if(palabras>1)
            masdedospalabras = true;*/
        
        //Si la cadena enviada por ModeloTxT tiene mas de 2 palabras entra al if
        if(palabras>1){ //masdedospalabras
            //Si son 2 palabras unicamente
            if(palabras == 2){
                //Si la primera palabra contine una sola letra. Ejemplo LG "G PRO" -> LGGPRO
                if(posiciones[1] == 1){
                    //Añade a cadena la primera palabra/letra
                    cadena = cadenaTextfield.substring(0, 1);
                    //Divida la cadena en 2 partes, donde encuentre un espacio
                    split = cadenaTextfield.indexOf(' ');
                    //Se crea una cadena con el contenido de la segunda palabra
                    cadenaDerecha = cadenaTextfield.substring(split+1);
                    if(cadenaDerecha.length() >= 3)
                         cadena += cadenaDerecha.substring(0, 3);
                    else
                        cadena += cadenaDerecha.substring(0, cadenaDerecha.length());
                }
                //Si la primera palabra tiene mas de 2 Caracteres
                if(posiciones[1] > 1){
                    split = cadenaTextfield.indexOf(' ');
                    //Separá las palabras y las asigna a variables independientes
                    cadenaIzquierda = cadenaTextfield.substring(0, split);
                    cadenaDerecha = cadenaTextfield.substring(split+1);
                    //Si ambos lados tienen mas de 2 caracteres cada uno, se les asignara a
                    //la cadena que regrese, 2 caracteres de cada palabra. 
                    //Ejemplo: SAMSUNG "S6 EDGE" -> SAMS6EDXXXXXX
                    if(cadenaIzquierda.length() >= 2 && cadenaDerecha.length() >= 2){
                        cadena = cadenaIzquierda.substring(0, 2);
                        cadena += cadenaDerecha.substring(0, 2);
                    }
                    //Si la primera palabra es mayor a 2 caracteres y la segunda solo tiene 1
                    if(cadenaIzquierda.length() >= 3 && cadenaDerecha.length() == 1){
                        cadena = cadenaIzquierda.substring(0, 3);
                        cadena += cadenaDerecha.substring(0, 1);
                    }
                }
            } //if(palabras == 2)
        }//fin if(masdedospalabras)
        //Si solo es una palabra
        else{
            if(cadenaTextfield.length() >= 4)
                cadena = cadenaTextfield.substring(0, 4);
            else{
                cadena = cadenaTextfield.substring(0, cadenaTextfield.length());
            }
        }
        return cadena;
    } // FIN FASE 2
    
    public String Fasetres(String cadenaTextfield){
        String cadena = "", cadenaDerecha, cadenaIzquierda;
        int[] posiciones = new int[5];
        int palabras = 1, contador = 1, posicion=0, tamano;
        int split;
        
        //boolean masdedospalabras = false;
        if(cadenaTextfield.equals("CLIP")){
            cadena = "clp";
        }else{
            for(int i = 0; i < cadenaTextfield.length(); i++){
                if(cadenaTextfield.charAt(i) == ' '){
                    palabras++;
                    posiciones[contador] = posicion;
                    contador++;
                }
                posicion++;
            }
            if(palabras>1){
                 if(posiciones[1] == 1){
                    //Añade a cadena la primera palabra/letra
                    cadena = cadenaTextfield.substring(0, 1);
                    //Divida la cadena en 2 partes, donde encuentre un espacio
                    split = cadenaTextfield.indexOf(' ');
                    //Se crea una cadena con el contenido de la segunda palabra
                    cadenaDerecha = cadenaTextfield.substring(split+1);
                    if(cadenaDerecha.length() >=2)
                         cadena += cadenaDerecha.substring(0, 2);
                    else
                        cadena += cadenaDerecha.substring(0, 1);
                }
                //Si la primera palabra tiene mas de 2 Caracteres
                if(posiciones[1] > 1){
                    split = cadenaTextfield.indexOf(' ');
                    //Separá las palabras y las asigna a variables independientes
                    cadenaIzquierda = cadenaTextfield.substring(0, split);
                    cadenaDerecha = cadenaTextfield.substring(split+1);
                    //Si ambos lados tienen mas de 2
                    if(cadenaIzquierda.length() >= 2 && cadenaDerecha.length() >= 2){
                        cadena = cadenaIzquierda.substring(0, 2);
                        cadena += cadenaDerecha.substring(0, 1);
                    }
                    //Si la primera palabra es mayor a 2 caracteres y la segunda solo tiene 1
                    if(cadenaIzquierda.length() >=2 && cadenaDerecha.length() == 1){
                        cadena = cadenaIzquierda.substring(0, 2);
                        cadena += cadenaDerecha.substring(0, 1);
                    }
                }
            }
            else{
                if(cadenaTextfield.length() >= 3)
                    cadena = cadenaTextfield.substring(0, 3);
                else{
                    cadena = cadenaTextfield.substring(0, cadenaTextfield.length());
                }
           }
        } 
        return cadena;
    }
}
