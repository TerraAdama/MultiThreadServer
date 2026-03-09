import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        String clientIP = socket.getInetAddress().toString();
        System.out.println("Thread " + threadName + " traite le client " + clientIP);
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Message recu : " + message);
                switch (message.toLowerCase()) {
                    case "hello":
                        writer.println("Bonjour client !");
                        break;
                    case "time":
                        writer.println("Date et heure actuelles : " + LocalDateTime.now());
                        break;
                    case "bye":
                        writer.println("Connexion fermée");
                        socket.close();
                        return;
                    default:
                        writer.println("Message recu : " + message);
                }
            }
        } catch (IOException e) {
            System.out.println("Connexion interrompue avec " + clientIP);
        }
    }
}