import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/*
El método "Runnable" es utilizada para cualquier clase cuya instancia está destinada
a ser ejecutada por un hilo y debe definir un método run sin argumentos
* */

public class Cliente {


    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JTextField text_mensaje = null;
    JTextArea area_texto = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;

    Socket socket = null;
    BufferedReader lector = null;
    PrintWriter writer = null;

    // Método importado del servidor
    public void hacerInterfaz(){
        //Título de la ventana
        ventana_chat = new JFrame("Cliente");
        // Texto del botón
        btn_enviar = new JButton("Envíar");
        // Ponemos el área del texto del mensaje que ocupe 4 columnas.
        text_mensaje = new JTextField(4);
        // Tamaño del texto que se va a ver
        area_texto = new JTextArea(10, 12);
        // Scroll para el area de la conversación
        scroll = new JScrollPane(area_texto);
        // Aquí creamos un JPanel, para poner todo en un mismo sitio, que llamaré contenedor
        contenedor_areachat = new JPanel();
        // Ajustamos las columnas del contenedor
        contenedor_areachat.setLayout(new GridLayout(1,1));
        contenedor_areachat.add(scroll);
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

        //Creamos el thread para escuchar el servidor desde el puerto que especificamos antes
        Thread principal = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Con esto escuchamos el servidor
                    socket = new Socket("localhost", 8080);

                    //Mantenemos los métodos activos para poder leer los mensajes entrantes
                    // y poder enviar nuestros mensajes
                    leer();
                    escribir();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        principal.start();
    }


    public void leer(){
        Thread leer_hilo = new Thread(new Runnable() {
            public void run(){
                try{
                    //Con esto nos permitirá poder leer todo lo que reciba el servidor
                    lector = new BufferedReader( new InputStreamReader(socket.getInputStream()) );

                    while(true){
                        //Todo lo que escriban lo recibo aquí...
                        String mensaje_recibido = lector.readLine();
                        area_texto.append("Servidor dice: " + mensaje_recibido + "\n");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        leer_hilo.start();
    }

    public void escribir(){

        Thread escribir_hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    // Inicializamos el printwriter para escribir en el chat
                    writer = new PrintWriter( socket.getOutputStream(), true );
                    // Este método según presionemos el botón, enviará el mensaje al servidor...
                    btn_enviar.addActionListener( new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String enviar_mensaje = text_mensaje.getText();
                            writer.println(enviar_mensaje);
                            text_mensaje.setText("");
                        }

                    } );
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        escribir_hilo.start();
    }

    // Constructor que llama a la interfaz, que a la vez llama los métodos.
    public Cliente(){
        hacerInterfaz();
    }


    public static void main(String[] args){

        // Llamamos al constructor para arrancar la aplicación.
        new Cliente();
    }

}
