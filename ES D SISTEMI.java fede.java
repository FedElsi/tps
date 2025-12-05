/*ELSI FEDERICO*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class CalcolatriceClient {
    private static final String HOST = "localhost";
    private static final int PORTA = 8844;
    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORTA);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== CALCOLATRICE REMOTA ===");
            System.out.println("Connesso a " + HOST + ":" + PORTA);
            System.out.println("Formato: NUMERO OPERAZIONE NUMERO (es: 10 + 5)");
            System.out.println("Operazioni: +  -  *  /");
            System.out.println("Scrivi 'quit' per uscire");
            while (true) {
                System.out.print("Calcolo > ");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("quit")) {
                    // Mando il comando di chiusura al server
                    out.println("QUIT");
                    String risposta = in.readLine();
                    if (risposta != null) {
                        System.out.println(risposta);
                    }
                    break;
                }
               if (input.isEmpty()) {
                    continue; // ignora righe vuote
                }
              // Invio richiesta di calcolo al server
                out.println(input);
               // Ricevo e stampo la risposta
                String risposta = in.readLine();
                if (risposta == null) {
                    System.out.println("Connessione chiusa dal server.");
                    break;
                }
               System.out.println(risposta);
            }
            System.out.println("Client terminato.");
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto: " + HOST);
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
        }
    }
}