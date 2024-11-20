import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket server;
    Socket client;
    final int porta;
    public Server() {
        server = null;
        client = null;
        porta = 1234;
    }
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        @Override
        public void run () {
            try {
                while ( true ) {
                    BufferedReader inDalClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    DataOutputStream outVersoClient = new DataOutputStream (clientSocket.getOutputStream());

                    String message = inDalClient.readLine () ;
                    System.out.println("Messaggio ricevuto da client : " + message);
                    outVersoClient.writeBytes (message + "\n");
                    outVersoClient.flush() ;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

    }
}