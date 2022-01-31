import javax.swing.*;
import java.awt.*;

public class Servidor {

    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JTextField text_mensaje = null;
    JTextArea area_texto = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;



    public Servidor(){
        hacerInterfaz();

    }

    // Construcción de interfaz para chat...
    public void hacerInterfaz(){
        //Título de la ventana
        ventana_chat = new JFrame("Servidor");
        // Texto del botón
        btn_enviar = new JButton("Envíar");
        // Ponemos el área del texto del mensaje que ocupe 4 columnas.
        text_mensaje = new JTextField(4);
        // Tamaño del texto que se va a ver
        area_texto = new JTextArea(10, 12);
        // Aquí creamos un JPanel, para poner todo en un mismo sitio, que llamaré contenedor
        contenedor_areachat = new JPanel();
        // Ajustamos las columnas del contenedor
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(area_texto);
        contenedor_btntxt = new JPanel();
        contenedor_btntxt.setLayout(new GridLayout(1,2));
        contenedor_btntxt.add(text_mensaje);
        contenedor_btntxt.add(btn_enviar);
        // Ponemos la ventana
        ventana_chat.setLayout(new BorderLayout());
        // Especificamos que parte quiero el area del texto y botón
        ventana_chat.add(area_texto, BorderLayout.NORTH);
        ventana_chat.add(contenedor_btntxt, BorderLayout.SOUTH);
        // Especificamos tamaño de la ventana, la visibilidad
        // quitamos la posiblidad del cambiar de tamaño de ventana
        // y el poder cerrarla
        ventana_chat.setSize(330,220);
        ventana_chat.setVisible(true);
        ventana_chat.setResizable(true);
        ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {

        //Instancia para que pueda arrancar el constructor, que arrancará el método.
        new Servidor();

    }

}
