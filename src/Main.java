import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        Socket ss = null;
        ServerSocket sv = new ServerSocket(8080);
        
        while(true){
            ss = sv.accept();
            Hilo worker = new Hilo(ss);
            worker.start();
        }


    }

    static class Hilo extends Thread{
        private Socket s = null;
        private ObjectInputStream ois = null;
        private ObjectOutputStream oos = null;
        DataInputStream in;

        public Hilo(Socket socket){
            this.s = socket;
        }

        public void run(){
            System.out.println("Conexión recibida desde" +  s.getInetAddress());

            try {
                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject("Introduce un nombre de usuario: ");
                String nombreUsuario = (String) ois.readObject();
                String respuesta = "Bienvenido [" + nombreUsuario + "] al chat de chiquitos.com" ;
                oos.writeObject(respuesta);
                System.out.println("Mensaje enviado correctamente " + s.getInetAddress());

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }finally{
                try {
                    if(oos != null){
                        oos.close();
                    }

                    if(ois != null){
                        ois.close();
                    }
                    System.out.println("Se acabo lo que se daba niñooo...");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }


        }
    }



}
