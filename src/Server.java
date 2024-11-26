import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private ServerSocket server;
    private Socket client;
    private final int porta;
    private final int[] votazioni;

    public Server() {
        server = null;
        client = null;
        porta = 1234;
        votazioni = new int[2];
    }
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        @Override
        public void run () {
            try {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream serverOutput = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String message = clientInput.readLine();
                    System.out.println("Messaggio ricevuto da client: " + message);

                    switch (message.toLowerCase()) {
                        case "a":
                            votazioni[0]++;
                            break;
                        case "b":
                            votazioni[1]++;
                            break;
                    }
                    String res = "";
                    if(votazioni[0] == votazioni[1]) {
                        res = "Parita'";
                    }
                    else if (votazioni[0] > votazioni[1]) {
                        res = "Sta vincendo l'opzione A";
                    }
                    else {
                        res = "Sta vincendo l'opzione B";
                    }

                    serverOutput.writeBytes(res + "\n");
                    serverOutput.flush();
                }
            } catch (IOException e) {
                System.out.println("Comunicazione interrotta con il client.");
            }
        }
    }

    public void attendi() {
        try {
            server = new ServerSocket(porta);
            server.setReuseAddress(true);
            System.out.println("Server in attesa di connessioni.");

            while(true) {
                client = server.accept();
                System.out.println("Nuova connessione: " + client);

                Thread clientHandler = new Thread(new ClientHandler(client));
                clientHandler.start();
            }

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante l'istanza del server.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.attendi();
    }
}