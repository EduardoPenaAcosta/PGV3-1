import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    // Implementación para el chat en modo gráfico
    JFrame ventana_chat = null;
    JButton btn_enviar = null;
    JTextField text_mensaje = null;
    JTextArea area_texto = null;
    JPanel contenedor_areachat = null;
    JPanel contenedor_btntxt = null;
    JScrollPane scroll = null;

    // Implementación para la creación del servidor
    ServerSocket servidor = null;
    Socket socket = null;
    BufferedReader lector = null;
    PrintWriter writer = null;

    //Puerto de donde leemos
    final int PORT = 8080;


    // Constructor de donde arrancaremos el servidor
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
        // Esto permitirá que tenga scroll en el chat
        scroll = new JScrollPane(area_texto);
        // Aquí creamos un JPanel, para poner todo en un mismo sitio, que llamaré contenedor
        contenedor_areachat = new JPanel();
        // Ajustamos las columnas del contenedor
        contenedor_areachat.setLayout(new GridLayout(1,1));
        // Aquí añadimos el scroll para que el chat no se coma el área de escribir
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

        // Creamos un thread donde abrimos el serivdor con el puerto
        Thread principal = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    servidor = new ServerSocket(PORT);

                    while(true){
                        //Aquí leera todas las conexiones que lleguen
                        socket = servidor.accept();

                        //Ejecutamos constantemente los dos métodos
                        // para que nos lleguen y enviamos los mensajes
                        leer();
                        escribir();
                    }

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
                        // Con esto escribimos en el area del texto el mensaje que nos escribe el cliente
                        area_texto.append("Cliente dice: " + mensaje_recibido + "\n");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        //Arrancamos el hilo
        leer_hilo.start();
    }

    //Método hilo
    public void escribir(){

        // Creamos un hilo con el método dentro
        Thread escribir_hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Con esto creamos el printwriter para escribir mediante el socket
                    writer = new PrintWriter( socket.getOutputStream(), true );
                    //Creamos un actionListener para controlar lo que escribimos en el area del texto
                    btn_enviar.addActionListener( new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Obtiene el mensaje y según presionemos el botón, se enviará
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

    public static void main(String[] args) {

        //Instancia para que pueda arrancar el constructor, que arrancará el método.
        new Servidor();

    }

}
